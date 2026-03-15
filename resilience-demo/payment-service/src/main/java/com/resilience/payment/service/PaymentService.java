package com.resilience.payment.service;

import com.resilience.payment.model.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RefreshScope
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Value("${investment.service.url:http://localhost:8083}")
    private String investmentServiceUrl;

    @Value("${resilience4j.retry.instances.investment.randomizedWaitFactor:0.3}")
    private double jitterFactor;

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "investment", fallbackMethod = "fallbackPayment")
    @Retry(name = "investment")
    public PaymentResponse processPayment(String orderId) {
        log.info("Processing payment for order: {} with jitter factor: {}", orderId, jitterFactor);
        String url = investmentServiceUrl + "/api/investment/process/" + orderId;
        @SuppressWarnings("unchecked")
        Map<String, Object> investmentResult = restTemplate.postForObject(url, null, Map.class);
        String investmentStatus = investmentResult != null
            ? investmentResult.getOrDefault("status", "UNKNOWN").toString()
            : "UNKNOWN";
        log.info("Investment result for order {}: {}", orderId, investmentStatus);
        return new PaymentResponse(
            orderId,
            "APPROVED",
            "Payment processed with investment status: " + investmentStatus,
            false,
            LocalDateTime.now()
        );
    }

    public PaymentResponse fallbackPayment(String orderId, Exception ex) {
        log.warn("Fallback triggered for order {} due to: {}", orderId, ex.getMessage());
        return new PaymentResponse(
            orderId,
            "DEGRADED",
            "Payment service operating in degraded mode: " + ex.getClass().getSimpleName(),
            true,
            LocalDateTime.now()
        );
    }
}
