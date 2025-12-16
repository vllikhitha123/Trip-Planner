package com.springboot.tripplanner.service;

import com.springboot.tripplanner.model.CancellationPolicy;
import com.springboot.tripplanner.model.Flight;
import com.springboot.tripplanner.model.Hotel;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.springboot.tripplanner.model.Constants.*;

@Service
public class CancellationPolicyService {

    // Map budget tiers to cut percentage
    private static final Map<String, Double> CUT_PERCENTAGE_MAP = Map.of(
            LOW_BUDGET, 10.0,    // Low tier: 10% cut → 90% refund
            MEDIUM_BUDGET, 20.0, // Medium tier: 20% cut → 80% refund
            HIGH_BUDGET, 30.0    // High tier: 30% cut → 70% refund
    );

    public CancellationPolicy calculate(
            Flight flight,
            Hotel hotel,
            double activitiesTotal,
            int travellers,
            String budgetTier) {

        CancellationPolicy policy = new CancellationPolicy();

        double totalBase = flight.getBasePrice() + hotel.getBasePrice() + activitiesTotal;

        // Get cut percentage, default to 20% if not found
        double cutPercentage = CUT_PERCENTAGE_MAP.getOrDefault(
                budgetTier.toUpperCase(), 20.0);

        double refundableAmountPerTraveller = totalBase * (1 - cutPercentage / 100);
        double totalRefundable = refundableAmountPerTraveller * travellers;

        int refundPercentage = (int) Math.round((refundableAmountPerTraveller / totalBase) * 100);

        policy.setRefundPercentage(refundPercentage);
        policy.setRefundableAmount(Math.round(totalRefundable));
        policy.setRiskLevel(riskLevel(refundPercentage));

        return policy;
    }

    private String riskLevel(int percent) {
        if (percent >= 75) return LOW_BUDGET;
        if (percent >= 50) return MEDIUM_BUDGET;
        return HIGH_BUDGET;
    }
}
