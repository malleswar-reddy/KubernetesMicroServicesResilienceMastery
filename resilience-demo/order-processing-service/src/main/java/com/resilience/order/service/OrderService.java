package com.resilience.order.service;

import com.resilience.order.client.PaymentFeignClient;
import com.resilience.order.model.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final PaymentFeignClient paymentFeignClient;

    public OrderService(PaymentFeignClient paymentFeignClient) {
        this.paymentFeignClient = paymentFeignClient;
    }

    public OrderResponse processOrder(String orderId) {
        log.info("Processing order: {}", orderId);
        try {
            Map<String, Object> paymentResult = paymentFeignClient.processPayment(orderId);
            String paymentStatus = paymentResult.getOrDefault("status", "UNKNOWN").toString();
            return new OrderResponse(
                orderId,
                "PROCESSED",
                "Order processed successfully",
                paymentStatus,
                LocalDateTime.now()
            );
        } catch (Exception ex) {
            log.warn("Payment service unavailable for order {}, using fallback: {}", orderId, ex.getMessage());
            return new OrderResponse(
                orderId,
                "PENDING",
                "Payment service temporarily unavailable, order queued",
                "PENDING",
                LocalDateTime.now()
            );
        }
    }
}
