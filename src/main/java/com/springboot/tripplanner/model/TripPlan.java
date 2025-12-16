package com.springboot.tripplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("trip_plans")
@JsonPropertyOrder({"id", "origin", "destination", "startDate", "endDate","travellers","budgetTier","options"})
public class TripPlan {

    @Id
    private String id;

    private String origin;
    private String destination;
    private String startDate;
    private String endDate;
    private int travellers;
    private String budgetTier;

    private List<TripOption> options;

    @Data
    @JsonPropertyOrder({ "optionId", "flight", "hotel", "activities", "tripType", "basePrice", "extraCharge", "totalPrice", "totalCostForTravellers","cancellationPolicy" })
    public static class TripOption {
        private String optionId;
        @JsonIgnore
        private String budgetTier;
        private Flight flight;
        private Hotel hotel;
        private String tripType;
        private double basePrice;     // raw subtotal
        private double extraCharge;   // markup/taxes
        private double totalPrice;  // for one traveller
        private double totalCostForTravellers;// final price
        private List<Activity> activities;
        private CancellationPolicy cancellationPolicy;
    }
}

