package com.churn.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="customers")
@Getter @Setter @NoArgsConstructor
public class Customer {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String gender; private Integer seniorCitizen; private String partner; private String dependents;
 private Integer tenure; private String phoneService; private String multipleLines; private String internetService;
 private String onlineSecurity; private String onlineBackup; private String deviceProtection; private String techSupport;
 private String streamingTV; private String streamingMovies; private String contract; private String paperlessBilling;
 private String paymentMethod; private Double monthlyCharges; private Double totalCharges;
}
