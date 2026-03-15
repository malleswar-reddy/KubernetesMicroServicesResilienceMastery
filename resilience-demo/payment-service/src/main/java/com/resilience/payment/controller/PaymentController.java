package com.resilience.payment.controller;

import com.resilience.payment.model.PaymentResponse;
import com.resilience.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable String orderId) {
        PaymentResponse response = paymentService.processPayment(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment Service is running");
    }
}
