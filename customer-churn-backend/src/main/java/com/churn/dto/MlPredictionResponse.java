package com.churn.dto;

public record MlPredictionResponse(String prediction, Double churn_probability, String risk_level) {}
