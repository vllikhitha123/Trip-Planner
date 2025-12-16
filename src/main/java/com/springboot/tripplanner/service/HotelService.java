package com.springboot.tripplanner.service;

import com.springboot.tripplanner.model.Hotel;
import com.springboot.tripplanner.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository repo;

    public List<Hotel> search(String city) {
        return repo.findByCity(city);
    }
}

