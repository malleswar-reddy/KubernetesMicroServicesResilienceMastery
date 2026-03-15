package com.resilience.investment.model;

import java.time.LocalDateTime;

public class InvestmentResponse {
    private String orderId;
    private String status;
    private String message;
    private long processingTimeMs;
    private LocalDateTime timestamp;

    public InvestmentResponse() {}

    public InvestmentResponse(String orderId, String status, String message, long processingTimeMs, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.processingTimeMs = processingTimeMs;
        this.timestamp = timestamp;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
