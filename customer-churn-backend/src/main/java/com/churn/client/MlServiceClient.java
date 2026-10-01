package com.churn.client;

import com.churn.dto.MlPredictionResponse;
import com.churn.entity.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MlServiceClient {
    private final RestClient client;

    public MlServiceClient(@Value("${ml.service.url}") String url) {
        this.client = RestClient.builder().baseUrl(url).build();
    }

    public MlPredictionResponse predict(Customer c) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("gender", c.getGender());
        body.put("SeniorCitizen", c.getSeniorCitizen());
        body.put("Partner", c.getPartner());
        body.put("Dependents", c.getDependents());
        body.put("tenure", c.getTenure());
        body.put("PhoneService", c.getPhoneService());
        body.put("MultipleLines", c.getMultipleLines());
        body.put("InternetService", c.getInternetService());
        body.put("OnlineSecurity", c.getOnlineSecurity());
        body.put("OnlineBackup", c.getOnlineBackup());
        body.put("DeviceProtection", c.getDeviceProtection());
        body.put("TechSupport", c.getTechSupport());
        body.put("StreamingTV", c.getStreamingTV());
        body.put("StreamingMovies", c.getStreamingMovies());
        body.put("Contract", c.getContract());
        body.put("PaperlessBilling", c.getPaperlessBilling());
        body.put("PaymentMethod", c.getPaymentMethod());
        body.put("MonthlyCharges", c.getMonthlyCharges());
        body.put("TotalCharges", c.getTotalCharges());

        return client.post().uri("/predict").body(body).retrieve().body(MlPredictionResponse.class);
    }
}
