package com.churn.kafka;
public record PredictionCompletedEvent(Long predictionId, String prediction, Double churnProbability, String riskLevel) {}
