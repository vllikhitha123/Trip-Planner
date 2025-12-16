package com.springboot.tripplanner.service;

import com.springboot.tripplanner.model.Activity;
import com.springboot.tripplanner.model.TripType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripTypeDetector {

    public TripType detect(List<Activity> activities) {

        boolean adventure = activities.stream()
                .anyMatch(a -> "ADVENTURE".equalsIgnoreCase(a.getType()));

        boolean culture = activities.stream()
                .anyMatch(a -> "CULTURE".equalsIgnoreCase(a.getType()));

        if (adventure && culture) return TripType.MIXED;
        if (adventure) return TripType.ADVENTURE;
        if (culture) return TripType.CULTURAL;

        return TripType.RELAX;
    }
}

