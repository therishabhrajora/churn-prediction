package com.churn.kafka;

import com.churn.service.PredictionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private final PredictionService service;

    public KafkaConsumer(PredictionService service) { this.service = service; }

    @KafkaListener(topics = "${app.kafka.prediction-result-topic}")
    public void consume(PredictionCompletedEvent event) { service.complete(event); }
}
