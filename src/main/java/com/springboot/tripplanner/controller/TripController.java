package com.springboot.tripplanner.controller;

import com.springboot.tripplanner.model.TripPlan;
import com.springboot.tripplanner.orchestrator.TripOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripOrchestrator orchestrator;

    @PostMapping("/plan")
    public TripPlan plan(@RequestBody TripPlan request) {
        try {
            return orchestrator.planTrip(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


