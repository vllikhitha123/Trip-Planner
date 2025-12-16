package com.springboot.tripplanner.service;

import com.springboot.tripplanner.model.Activity;
import com.springboot.tripplanner.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository repo;

    public List<Activity> search(String city) {
        return repo.findByCity(city);
    }
}
