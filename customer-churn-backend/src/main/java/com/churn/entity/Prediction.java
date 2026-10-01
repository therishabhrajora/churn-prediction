package com.churn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="predictions")
@Getter @Setter @NoArgsConstructor
public class Prediction {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Customer customer;
 private String prediction; private Double churnProbability; private String riskLevel; private String status;
 private LocalDateTime createdAt;
}
