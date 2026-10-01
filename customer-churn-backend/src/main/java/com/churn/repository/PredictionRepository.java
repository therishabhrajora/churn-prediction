package com.churn.repository;
import com.churn.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PredictionRepository extends JpaRepository<Prediction,Long> { List<Prediction> findByCustomerIdOrderByCreatedAtDesc(Long customerId); }
