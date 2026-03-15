package com.resilience.jitter.controller;

import com.resilience.jitter.model.AlertmanagerWebhook;
import com.resilience.jitter.service.JitterTunerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jitter")
public class JitterTunerController {

    private final JitterTunerService jitterTunerService;

    public JitterTunerController(JitterTunerService jitterTunerService) {
        this.jitterTunerService = jitterTunerService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> receiveWebhook(@RequestBody AlertmanagerWebhook webhook) {
        double newJitter = jitterTunerService.processAlert(webhook);
        return ResponseEntity.ok(Map.of(
            "status", "processed",
            "newJitterFactor", newJitter,
            "message", "Jitter factor updated successfully"
        ));
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentJitter() {
        return ResponseEntity.ok(Map.of(
            "currentJitterFactor", jitterTunerService.getCurrentJitter()
        ));
    }

    @PostMapping("/set")
    public ResponseEntity<Map<String, Object>> setJitter(@RequestParam double value) {
        jitterTunerService.setCurrentJitter(value);
        return ResponseEntity.ok(Map.of(
            "status", "updated",
            "jitterFactor", jitterTunerService.getCurrentJitter()
        ));
    }
}
