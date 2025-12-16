package com.springboot.tripplanner.controller;

import com.springboot.tripplanner.model.*;
import com.springboot.tripplanner.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.springboot.tripplanner.model.Constants.*;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataLoaderController {

    private final FlightRepository flightRepo;
    private final HotelRepository hotelRepo;
    private final ActivityRepository activityRepo;
    private final PricingRuleRepository pricingRuleRepo;
    private final TripPlanRepository tripPlanRepo;

    @PostMapping("/loadAll")
    public String loadAllDummyData() {

        // --- Flights ---
        List<Flight> flights = Arrays.asList(
                Flight.builder().origin("DEL").destination("PARIS").date("2025-12-20")
                        .basePrice(400).flightClass(ECONOMY).airline("AF").availableSeats(20).build(),
                Flight.builder().origin("DEL").destination("PARIS").date("2025-12-20")
                        .basePrice(900).flightClass(BUSINESS).airline("AF").availableSeats(5).build(),
                Flight.builder().origin("DEL").destination("PARIS").date("2025-12-21")
                        .basePrice(420).flightClass(ECONOMY).airline("AI").availableSeats(15).build(),
                Flight.builder().origin("DEL").destination("LONDON").date("2025-12-20")
                        .basePrice(350).flightClass(ECONOMY).airline("BA").availableSeats(25).build(),
                Flight.builder().origin("DEL").destination("LONDON").date("2025-12-20")
                        .basePrice(800).flightClass(BUSINESS).airline("BA").availableSeats(8).build()
        );
        flightRepo.saveAll(flights);

        // --- Hotels ---
        List<Hotel> hotels = Arrays.asList(
                Hotel.builder().city("PARIS").name("Hotel Comfort").stars(3)
                        .breakfastIncluded(true).freeCancellation(true).basePrice(300).build(),
                Hotel.builder().city("PARIS").name("Hotel Luxury").stars(5)
                        .breakfastIncluded(true).freeCancellation(false).basePrice(800).build(),
                Hotel.builder().city("PARIS").name("Hotel Midrange").stars(4)
                        .breakfastIncluded(false).freeCancellation(true).basePrice(500).build(),
                Hotel.builder().city("LONDON").name("London Budget").stars(3)
                        .breakfastIncluded(true).freeCancellation(true).basePrice(250).build(),
                Hotel.builder().city("LONDON").name("London Deluxe").stars(5)
                        .breakfastIncluded(true).freeCancellation(false).basePrice(900).build()
        );
        hotelRepo.saveAll(hotels);

        // --- Activities ---
        List<Activity> activities = Arrays.asList(
                Activity.builder().city("PARIS").name("Louvre Tour").type("CULTURE")
                        .timeSlot("Morning").price(50).build(),
                Activity.builder().city("PARIS").name("Eiffel Tower Visit").type("TOUR")
                        .timeSlot("Afternoon").price(30).build(),
                Activity.builder().city("PARIS").name("Seine Cruise").type("ADVENTURE")
                        .timeSlot("Evening").price(70).build(),
                Activity.builder().city("PARIS").name("Montmartre Walk").type("CULTURE")
                        .timeSlot("Morning").price(40).build(),
                Activity.builder().city("LONDON").name("London Eye Ride").type("TOUR")
                        .timeSlot("Afternoon").price(35).build(),
                Activity.builder().city("LONDON").name("Thames Cruise").type("ADVENTURE")
                        .timeSlot("Evening").price(60).build()
        );
        activityRepo.saveAll(activities);

        // --- Pricing Rules ---
        List<PricingRuleModel> rules = Arrays.asList(
                PricingRuleModel.builder().type(SEASONAL).destination("PARIS")
                        .startDate("2025-12-20").endDate("2026-01-05").multiplier(1.20).build(),
                PricingRuleModel.builder().type(SEASONAL).destination("LONDON")
                        .startDate("2025-12-20").endDate("2026-01-05").multiplier(1.15).build(),
                PricingRuleModel.builder().type(BUDGET_TIER).budgetTier("HIGH").marginPercentage(0.18).build(),
                PricingRuleModel.builder().type(BUDGET_TIER).budgetTier("MEDIUM").marginPercentage(0.10).build(),
                PricingRuleModel.builder().type(BUDGET_TIER).budgetTier("LOW").marginPercentage(0.05).build(),
                PricingRuleModel.builder().type(CITY_POPULARITY).destination("PARIS").multiplier(1.15).build(),
                PricingRuleModel.builder().type(CITY_POPULARITY).destination("LONDON").multiplier(1.10).build(),
                PricingRuleModel.builder().type(LONG_STAY).multiplier(0.90).build(),
                PricingRuleModel.builder().type(LOYALTY).multiplier(0.85).build(),
                PricingRuleModel.builder().type(WEEKEND).multiplier(1.10).build()
        );
        pricingRuleRepo.saveAll(rules);

        // --- Sample Trip Plans ---
        TripPlan sampleTrip1 = new TripPlan();
        sampleTrip1.setOrigin("DEL");
        sampleTrip1.setDestination("PARIS");
        sampleTrip1.setStartDate("2025-12-20");
        sampleTrip1.setEndDate("2025-12-25");
        sampleTrip1.setTravellers(2);
        sampleTrip1.setBudgetTier("MEDIUM");
        sampleTrip1.setOptions(Collections.emptyList());

        TripPlan sampleTrip2 = new TripPlan();
        sampleTrip2.setOrigin("DEL");
        sampleTrip2.setDestination("LONDON");
        sampleTrip2.setStartDate("2025-12-20");
        sampleTrip2.setEndDate("2025-12-27");
        sampleTrip2.setTravellers(1);
        sampleTrip2.setBudgetTier("HIGH");
        sampleTrip2.setOptions(Collections.emptyList());

        tripPlanRepo.saveAll(Arrays.asList(sampleTrip1, sampleTrip2));

        return "All dummy data loaded successfully";
    }
}
