package com.springboot.tripplanner.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Builder
@Document("pricing_rules")
public class PricingRuleModel {
    @Id
    private String id;

    private String type;          // SEASONAL, BUDGET_TIER, CITY_POPULARITY, LONG_STAY, WEEKEND, LOYALTY
    private String destination;   // optional for rules specific to a destination
    private String budgetTier;    // LOW, MEDIUM, HIGH (for BUDGET_TIER)
    private String startDate;     // for SEASONAL
    private String endDate;       // for SEASONAL
    private Double multiplier;    // for rules that multiply the base price
    private Double marginPercentage; // for BUDGET_TIER (additive)
    private Long minDays;         // for LONG_STAY
    private Double popularityFactor; // for CITY_POPULARITY
    private Boolean loyaltyEligible; // for LOYALTY
}
