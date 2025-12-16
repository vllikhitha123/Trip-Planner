package com.springboot.tripplanner.model;


import lombok.Data;

@Data
public class CancellationPolicy {

    private double refundableAmount;
    private int refundPercentage;
    private String riskLevel; // LOW / MEDIUM / HIGH
}

