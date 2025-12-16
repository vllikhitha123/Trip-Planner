package com.springboot.tripplanner.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("activities")
public class Activity {
    @Id
    private String id;
    private String city;
    private String name;
    private String type;      // TOUR, CULTURE, ADVENTURE
    private String timeSlot;  // Morning, Afternoon, Evening
    private double price;
}


