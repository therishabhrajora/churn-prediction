package com.churn.dto;
public record PredictionResponse(Long id, Long customerId, String prediction, Double churnProbability, String riskLevel, String status) {}
