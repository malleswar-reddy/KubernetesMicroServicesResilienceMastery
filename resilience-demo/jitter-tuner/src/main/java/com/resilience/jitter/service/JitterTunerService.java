package com.resilience.jitter.service;

import com.resilience.jitter.model.AlertmanagerWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class JitterTunerService {

    private static final Logger log = LoggerFactory.getLogger(JitterTunerService.class);

    private static final String HIGH_RETRY_STORM_ALERT = "HighRetryStorm";

    @Value("${jitter.min:0.2}")
    private double jitterMin;

    @Value("${jitter.max:0.5}")
    private double jitterMax;

    @Value("${jitter.increment:0.1}")
    private double jitterIncrement;

    @Value("${jitter.decrement:0.05}")
    private double jitterDecrement;

    private final AtomicReference<Double> currentJitter;

    public JitterTunerService(@Value("${jitter.initial:0.3}") double initialJitter) {
        this.currentJitter = new AtomicReference<>(initialJitter);
    }

    public double processAlert(AlertmanagerWebhook webhook) {
        if (webhook.getAlerts() == null || webhook.getAlerts().isEmpty()) {
            log.info("No alerts in webhook, decreasing jitter");
            return decreaseJitter();
        }

        boolean hasHighRetryStorm = webhook.getAlerts().stream()
            .filter(a -> a.getLabels() != null)
            .anyMatch(a -> HIGH_RETRY_STORM_ALERT.equals(a.getLabels().get("alertname")));

        if (hasHighRetryStorm) {
            log.warn("HighRetryStorm alert detected, increasing jitter");
            return increaseJitter();
        } else {
            log.info("No HighRetryStorm detected, decreasing jitter");
            return decreaseJitter();
        }
    }

    private double increaseJitter() {
        return currentJitter.updateAndGet(current -> {
            double newValue = Math.min(current + jitterIncrement, jitterMax);
            log.info("Jitter increased: {} -> {}", current, newValue);
            applyJitterChange(newValue);
            return newValue;
        });
    }

    private double decreaseJitter() {
        return currentJitter.updateAndGet(current -> {
            double newValue = Math.max(current - jitterDecrement, jitterMin);
            log.info("Jitter decreased: {} -> {}", current, newValue);
            applyJitterChange(newValue);
            return newValue;
        });
    }

    private void applyJitterChange(double newJitter) {
        log.info("Applying jitter change: randomizedWaitFactor={}", newJitter);
        log.info("In Kubernetes mode: would update ConfigMap 'payment-resilience-config' key 'randomizedWaitFactor'");
    }

    public double getCurrentJitter() {
        return currentJitter.get();
    }

    public void setCurrentJitter(double jitter) {
        double clamped = Math.max(jitterMin, Math.min(jitterMax, jitter));
        currentJitter.set(clamped);
        log.info("Jitter manually set to: {}", clamped);
    }
}
