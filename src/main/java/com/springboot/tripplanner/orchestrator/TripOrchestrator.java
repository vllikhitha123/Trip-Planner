package com.springboot.tripplanner.orchestrator;

import com.springboot.tripplanner.model.*;
import com.springboot.tripplanner.pricing.PricingEngine;
import com.springboot.tripplanner.repository.TripPlanRepository;
import com.springboot.tripplanner.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.springboot.tripplanner.model.Constants.*;

@Service
@RequiredArgsConstructor
public class TripOrchestrator {

    private final FlightService flightService;
    private final HotelService hotelService;
    private final ActivityService activityService;
    private final PricingEngine pricingEngine;
    private final TripPlanRepository tripRepo;
    private final TripTypeDetector tripTypeDetector;
    private final CancellationPolicyService cancellationService;


    private final Map<String, Function<List<Flight>, Flight>> flightStrategyMap =
            new HashMap<>() {{

                put(CHEAPEST, flights ->
                        flights.stream()
                                .min(Comparator.comparingDouble(Flight::getBasePrice))
                                .orElseGet(flights::getFirst));

                put(BUSINESS_FIRST, flights ->
                        flights.stream()
                                .filter(f -> BUSINESS.equalsIgnoreCase(f.getFlightClass()))
                                .findFirst()
                                .orElseGet(() ->
                                        flights.stream()
                                                .min(Comparator.comparingDouble(Flight::getBasePrice))
                                                .orElse(flights.getFirst()))
                );

                put(AIRLINE_AF, flights ->
                        flights.stream()
                                .filter(f -> "AF".equalsIgnoreCase(f.getAirline()))
                                .findFirst()
                                .orElse(flights.getFirst()));
            }};


    private Hotel chooseHotel(List<Hotel> hotels,
                              int minStars,
                              int maxStars,
                              boolean breakfast,
                              boolean freeCancel) {

        return hotels.stream()
                .filter(h -> h.getStars() >= minStars && h.getStars() <= maxStars)
                .filter(h -> !breakfast || h.isBreakfastIncluded())
                .filter(h -> !freeCancel || h.isFreeCancellation())
                .min(Comparator.comparingDouble(Hotel::getBasePrice))
                .orElse(hotels.getFirst());
    }

    private Function<String, List<Activity>> activityPicker(List<Activity> activities) {
        return type -> activities.stream()
                .filter(a -> a.getType().equalsIgnoreCase(type))
                .sorted(Comparator.comparingDouble(Activity::getPrice))
                .distinct()
                .limit(1)
                .collect(Collectors.toList());
    }

    private List<Activity> mergeActivities(List<List<Activity>> lists, int limit) {
        return lists.stream()
                .flatMap(List::stream)
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private final Map<String, Function<TripInventory, TripPlan.TripOption>> budgetTierSelector =
            new HashMap<>();

    {
        // ---- LOW -----------------------------------------------------------
        budgetTierSelector.put(LOW_BUDGET, inv -> {

            Flight flight = flightStrategyMap.get(CHEAPEST).apply(inv.getFlights());
            Hotel hotel = chooseHotel(inv.getHotels(), 1, 3, false, true);
            List<Activity> activities = activityPicker(inv.getActivities()).apply(TOUR);

            return buildOption(LOW_BUDGET, flight, hotel, activities);
        });

        // ---- MEDIUM --------------------------------------------------------
        budgetTierSelector.put(MEDIUM_BUDGET, inv -> {

            Function<String, List<Activity>> pick = activityPicker(inv.getActivities());

            Flight f = flightStrategyMap.get(AIRLINE_AF).apply(inv.getFlights());
            Hotel h = chooseHotel(inv.getHotels(), 2, 4, true, false);

            List<Activity> acts = mergeActivities(
                    Arrays.asList(
                            pick.apply(ADVENTURE),
                            pick.apply(CULTURE),
                            pick.apply(TOUR)
                    ),
                    2
            );

            return buildOption(MEDIUM_BUDGET, f, h, acts);
        });

        // ---- HIGH -----------------------------------------------------------
        budgetTierSelector.put(HIGH_BUDGET, inv -> {

            Function<String, List<Activity>> pick = activityPicker(inv.getActivities());

            Flight flight = flightStrategyMap.get(BUSINESS_FIRST).apply(inv.getFlights());
            Hotel hotel = chooseHotel(inv.getHotels(), 4, 5, true, false);

            List<Activity> acts = mergeActivities(
                    Arrays.asList(
                            pick.apply(ADVENTURE),
                            pick.apply(CULTURE),
                            pick.apply(TOUR)
                    ),
                    3
            );

            return buildOption(HIGH_BUDGET, flight, hotel, acts);
        });
    }


    public TripPlan planTrip(TripPlan request) {

        CompletableFuture<List<Flight>> flightsFuture =
                CompletableFuture.supplyAsync(() -> flightService.search(request.getOrigin(), request.getDestination()));

        CompletableFuture<List<Hotel>> hotelsFuture =
                CompletableFuture.supplyAsync(() -> hotelService.search(request.getDestination()));

        CompletableFuture<List<Activity>> activitiesFuture =
                CompletableFuture.supplyAsync(() -> activityService.search(request.getDestination()));

        CompletableFuture.allOf(flightsFuture, hotelsFuture, activitiesFuture).join();

        try {
            List<Flight> flights = flightsFuture.get();
            List<Hotel> hotels = hotelsFuture.get();
            List<Activity> activities = activitiesFuture.get();

            if (flights.isEmpty() || hotels.isEmpty() || activities.isEmpty()) {
                throw new RuntimeException("Inventory missing. Please load sample data.");
            }

            TripInventory inv = new TripInventory(flights, hotels, activities);

            String tier = Optional.ofNullable(request.getBudgetTier())
                    .map(String::toUpperCase)
                    .orElse(MEDIUM_BUDGET);

            TripPlan.TripOption option =
                    budgetTierSelector
                            .getOrDefault(tier, budgetTierSelector.get(MEDIUM_BUDGET))
                            .apply(inv);

            double base = option.getFlight().getBasePrice()
                    + option.getHotel().getBasePrice()
                    + option.getActivities().stream().mapToDouble(Activity::getPrice).sum();

            double finalPrice = pricingEngine.calculatePrice(base, request.getDestination(), tier);

            CancellationPolicy policy = cancellationService.calculate(
                    option.getFlight(),
                    option.getHotel(),
                    option.getActivities().stream().mapToDouble(Activity::getPrice).sum(),
                    request.getTravellers(),
                    request.getBudgetTier()
            );

            option.setBasePrice(base);
            option.setTotalPrice(Math.round(finalPrice));
            option.setExtraCharge(Math.round(finalPrice - base));
            option.setTotalCostForTravellers(Math.round(finalPrice * request.getTravellers()));
            option.setTripType(
                    tripTypeDetector
                            .detect(option.getActivities())
                            .name());
            option.setCancellationPolicy(policy);

            request.setOptions(Collections.singletonList(option));
            request.setBudgetTier(tier);

            return tripRepo.save(request);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static TripPlan.TripOption buildOption(String tier, Flight flight, Hotel hotel, List<Activity> activities) {
        TripPlan.TripOption option = new TripPlan.TripOption();
        option.setOptionId(UUID.randomUUID().toString());
        option.setBudgetTier(tier);
        option.setFlight(flight);
        option.setHotel(hotel);
        option.setActivities(activities);
        return option;
    }
}
