package com.churn.kafka;

import com.churn.client.MlServiceClient;
import com.churn.dto.MlPredictionResponse;
import com.churn.entity.Customer;
import com.churn.repository.CustomerRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionWorker {
    private final CustomerRepository customers;
    private final MlServiceClient ml;
    private final KafkaTemplate<String, Object> kafka;
    private final String resultTopic;

    public PredictionWorker(CustomerRepository customers, MlServiceClient ml,
                            KafkaTemplate<String, Object> kafka,
                            @Value("${app.kafka.prediction-result-topic}") String resultTopic) {
        this.customers = customers;
        this.ml = ml;
        this.kafka = kafka;
        this.resultTopic = resultTopic;
    }

    @KafkaListener(topics = "${app.kafka.prediction-request-topic}")
    public void consume(PredictionRequestedEvent event) {
        Customer customer = customers.findById(event.customerId()).orElseThrow();
        MlPredictionResponse result = ml.predict(customer);
        kafka.send(resultTopic, String.valueOf(event.customerId()),
                new PredictionCompletedEvent(event.predictionId(), result.prediction(),
                        result.churn_probability(), result.risk_level()));
    }
}
