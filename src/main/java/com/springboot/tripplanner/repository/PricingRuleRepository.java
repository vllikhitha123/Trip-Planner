package com.springboot.tripplanner.repository;


import com.springboot.tripplanner.model.PricingRuleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PricingRuleRepository extends MongoRepository<PricingRuleModel, String> {

}

