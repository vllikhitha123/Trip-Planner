package com.springboot.tripplanner.repository;

import com.springboot.tripplanner.model.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HotelRepository extends MongoRepository<Hotel, String> {
    List<Hotel> findByCity(String city);
}

