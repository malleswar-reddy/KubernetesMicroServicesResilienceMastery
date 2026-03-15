package com.resilience.order.model;

import java.time.LocalDateTime;

public class OrderResponse {
    private String orderId;
    private String status;
    private String message;
    private String paymentStatus;
    private LocalDateTime timestamp;

    public OrderResponse() {}

    public OrderResponse(String orderId, String status, String message, String paymentStatus, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.paymentStatus = paymentStatus;
        this.timestamp = timestamp;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
