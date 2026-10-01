package com.churn.kafka;
public record PredictionRequestedEvent(Long predictionId, Long customerId) {}
