package com.churn.service;

import com.churn.dto.PredictionResponse;
import com.churn.entity.Customer;
import com.churn.entity.Prediction;
import com.churn.kafka.KafkaProducer;
import com.churn.kafka.PredictionCompletedEvent;
import com.churn.kafka.PredictionRequestedEvent;
import com.churn.repository.PredictionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PredictionService {
    private final CustomerService customers;
    private final PredictionRepository repo;
    private final KafkaProducer producer;
    private final RedisTemplate<String, Object> redis;

    public PredictionService(CustomerService customers, PredictionRepository repo,
                             KafkaProducer producer, RedisTemplate<String, Object> redis) {
        this.customers = customers; this.repo = repo; this.producer = producer; this.redis = redis;
    }

    public PredictionResponse request(Long customerId) {
        Customer customer = customers.get(customerId);
        Prediction p = new Prediction();
        p.setCustomer(customer);
        p.setStatus("PENDING");
        p.setCreatedAt(LocalDateTime.now());
        p = repo.save(p);
        producer.publish(new PredictionRequestedEvent(p.getId(), customerId));
        return map(p);
    }

    public PredictionResponse get(Long predictionId) {
        Object cached = null;
        // Prediction results are cached by customer, while DB remains the source of truth.
        Prediction p = repo.findById(predictionId)
                .orElseThrow(() -> new IllegalArgumentException("Prediction not found: " + predictionId));
        return map(p);
    }

    public PredictionResponse latest(Long customerId) {
        Object cached = redis.opsForValue().get("prediction:" + customerId);
        if (cached instanceof PredictionResponse response) return response;
        return repo.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .findFirst().map(this::map).orElse(null);
    }

    public List<PredictionResponse> history(Long customerId) {
        return repo.findByCustomerIdOrderByCreatedAtDesc(customerId).stream().map(this::map).toList();
    }

    public void complete(PredictionCompletedEvent e) {
        Prediction p = repo.findById(e.predictionId()).orElseThrow();
        p.setPrediction(e.prediction());
        p.setChurnProbability(e.churnProbability());
        p.setRiskLevel(e.riskLevel());
        p.setStatus("COMPLETED");
        p = repo.save(p);
        redis.opsForValue().set("prediction:" + p.getCustomer().getId(), map(p));
    }

    private PredictionResponse map(Prediction p) {
        return new PredictionResponse(p.getId(), p.getCustomer().getId(), p.getPrediction(),
                p.getChurnProbability(), p.getRiskLevel(), p.getStatus());
    }
}
