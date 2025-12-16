package com.springboot.tripplanner.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("hotels")
public class Hotel {
    @Id
    private String id;
    private String city;
    private String name;
    private int stars;
    private boolean breakfastIncluded;
    private boolean freeCancellation;
    private double basePrice;
}


