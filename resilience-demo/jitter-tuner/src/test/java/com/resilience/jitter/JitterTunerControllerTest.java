package com.resilience.jitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resilience.jitter.controller.JitterTunerController;
import com.resilience.jitter.model.AlertmanagerWebhook;
import com.resilience.jitter.service.JitterTunerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JitterTunerController.class)
public class JitterTunerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JitterTunerService jitterTunerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testReceiveWebhookHighRetryStorm() throws Exception {
        when(jitterTunerService.processAlert(any(AlertmanagerWebhook.class))).thenReturn(0.4);

        AlertmanagerWebhook webhook = new AlertmanagerWebhook();
        AlertmanagerWebhook.Alert alert = new AlertmanagerWebhook.Alert();
        alert.setLabels(Map.of("alertname", "HighRetryStorm", "severity", "warning"));
        webhook.setAlerts(List.of(alert));
        webhook.setStatus("firing");

        mockMvc.perform(post("/api/jitter/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(webhook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("processed"))
            .andExpect(jsonPath("$.newJitterFactor").value(0.4));
    }

    @Test
    public void testGetCurrentJitter() throws Exception {
        when(jitterTunerService.getCurrentJitter()).thenReturn(0.3);

        mockMvc.perform(get("/api/jitter/current"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentJitterFactor").value(0.3));
    }

    @Test
    public void testSetJitter() throws Exception {
        when(jitterTunerService.getCurrentJitter()).thenReturn(0.4);

        mockMvc.perform(post("/api/jitter/set").param("value", "0.4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("updated"))
            .andExpect(jsonPath("$.jitterFactor").value(0.4));
    }
}
