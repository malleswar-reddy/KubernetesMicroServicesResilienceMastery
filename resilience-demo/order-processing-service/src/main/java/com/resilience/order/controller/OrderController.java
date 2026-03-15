package com.resilience.order.controller;

import com.resilience.order.model.OrderResponse;
import com.resilience.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<OrderResponse> processOrder(@PathVariable String orderId) {
        OrderResponse response = orderService.processOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Processing Service is running");
    }
}
