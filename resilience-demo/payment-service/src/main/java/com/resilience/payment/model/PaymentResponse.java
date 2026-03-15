package com.resilience.payment.model;

import java.time.LocalDateTime;

public class PaymentResponse {
    private String orderId;
    private String status;
    private String message;
    private boolean fallback;
    private LocalDateTime timestamp;

    public PaymentResponse() {}

    public PaymentResponse(String orderId, String status, String message, boolean fallback, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.fallback = fallback;
        this.timestamp = timestamp;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isFallback() { return fallback; }
    public void setFallback(boolean fallback) { this.fallback = fallback; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
