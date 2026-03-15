package com.resilience.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;

@FeignClient(name = "payment-service", url = "${payment.service.url:http://localhost:8082}")
public interface PaymentFeignClient {
    @PostMapping("/api/payment/process/{orderId}")
    Map<String, Object> processPayment(@PathVariable("orderId") String orderId);
}
