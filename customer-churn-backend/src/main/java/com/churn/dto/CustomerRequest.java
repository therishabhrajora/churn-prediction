package com.churn.dto;
import jakarta.validation.constraints.*;
public record CustomerRequest(String gender,@NotNull Integer seniorCitizen,String partner,String dependents,@NotNull Integer tenure,String phoneService,String multipleLines,String internetService,String onlineSecurity,String onlineBackup,String deviceProtection,String techSupport,String streamingTV,String streamingMovies,String contract,String paperlessBilling,String paymentMethod,@NotNull @PositiveOrZero Double monthlyCharges,@NotNull @PositiveOrZero Double totalCharges) {}
