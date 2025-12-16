package com.springboot.tripplanner.pricing;

import com.springboot.tripplanner.model.PricingRuleModel;
import com.springboot.tripplanner.repository.PricingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.BiFunction;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.springboot.tripplanner.model.Constants.*;

@Service
@RequiredArgsConstructor
public class PricingEngine {

    private final PricingRuleRepository ruleRepo;

    private static final Map<String, BiFunction<Double, PricingRuleModel, Double>> ruleFunctions =
            Map.of(
                    SEASONAL, (price, rule) -> price * rule.getMultiplier(),
                    CITY_POPULARITY, (price, rule) -> price * rule.getMultiplier(),
                    LONG_STAY, (price, rule) -> price * rule.getMultiplier(),
                    WEEKEND, (price, rule) -> price * rule.getMultiplier(),
                    LOYALTY, (price, rule) -> price * rule.getMultiplier(),
                    BUDGET_TIER, (price, rule) -> price + (price * rule.getMarginPercentage())
            );

    public double calculatePrice(double basePrice, String destination, String tier) {

        double price = basePrice;

        Map<String, PricingRuleModel> effectiveRules =
                ruleRepo.findAll().stream()
                        .filter(r -> isApplicable(r, destination, tier))
                        .collect(Collectors.toMap(
                                PricingRuleModel::getType,
                                Function.identity(),
                                (r1, r2) -> r1 // keep first
                        ));

        for (PricingRuleModel rule : effectiveRules.values()) {

            BiFunction<Double, PricingRuleModel, Double> fn =
                    ruleFunctions.get(rule.getType());

            if (fn != null) {
                price = fn.apply(price, rule);
            }
        }

        return price;
    }

    private boolean isApplicable(PricingRuleModel rule, String destination, String tier) {

        if (rule.getDestination() != null &&
                !rule.getDestination().equalsIgnoreCase(destination))
            return false;

        return rule.getBudgetTier() == null ||
                rule.getBudgetTier().equalsIgnoreCase(tier);
    }
}
