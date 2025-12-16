package com.springboot.tripplanner.repository;

import com.springboot.tripplanner.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity, String> {
    List<Activity> findByCity(String city);
}
