package com.resilience.investment.controller;

import com.resilience.investment.model.InvestmentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/investment")
public class InvestmentController {

    private static final Logger log = LoggerFactory.getLogger(InvestmentController.class);

    @Value("${investment.simulation.minDelayMs:500}")
    private int minDelayMs;

    @Value("${investment.simulation.maxDelayMs:3000}")
    private int maxDelayMs;

    @Value("${investment.simulation.failureRate:0.25}")
    private double failureRate;

    @PostMapping("/process/{orderId}")
    public ResponseEntity<InvestmentResponse> processInvestment(@PathVariable String orderId) throws InterruptedException {
        long delayMs = ThreadLocalRandom.current().nextLong(minDelayMs, (long) maxDelayMs + 1);
        log.info("Processing investment for order: {}, simulating {}ms delay", orderId, delayMs);
        Thread.sleep(delayMs);

        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            log.warn("Simulating failure for order: {}", orderId);
            throw new RuntimeException("Investment service simulated failure for order: " + orderId);
        }

        InvestmentResponse response = new InvestmentResponse(
            orderId,
            "COMPLETED",
            "Investment processed successfully",
            delayMs,
            LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Investment Service is running");
    }
}
