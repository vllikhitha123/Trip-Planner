package com.springboot.tripplanner.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("flights")
@Builder
public class Flight {
    @Id
    private String id;
    private String origin;
    private String destination;
    private String date;           // ISO date string
    private String airline;        // e.g., "Air France"
    private String flightClass;    // ECONOMY, BUSINESS
    private int availableSeats;
    private double basePrice;
}


