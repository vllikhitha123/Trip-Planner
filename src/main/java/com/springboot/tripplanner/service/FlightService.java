package com.springboot.tripplanner.service;

import com.springboot.tripplanner.model.Flight;
import com.springboot.tripplanner.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository repo;

    public List<Flight> search(String origin, String destination) {
        return repo.findByOriginAndDestination(origin, destination);
    }
}

