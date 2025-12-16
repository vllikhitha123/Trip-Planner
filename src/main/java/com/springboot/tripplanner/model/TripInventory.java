package com.springboot.tripplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TripInventory {
    private List<Flight> flights;
    private List<Hotel> hotels;
    private List<Activity> activities;
}
