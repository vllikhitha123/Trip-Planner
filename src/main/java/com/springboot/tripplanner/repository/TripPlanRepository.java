package com.springboot.tripplanner.repository;

import com.springboot.tripplanner.model.TripPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TripPlanRepository extends MongoRepository<TripPlan, String> {
}
