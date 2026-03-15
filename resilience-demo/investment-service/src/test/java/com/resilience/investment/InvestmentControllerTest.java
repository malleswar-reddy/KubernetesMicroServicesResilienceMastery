package com.resilience.investment;

import com.resilience.investment.controller.InvestmentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestmentController.class)
@TestPropertySource(properties = {
    "investment.simulation.minDelayMs=0",
    "investment.simulation.maxDelayMs=1",
    "investment.simulation.failureRate=0.0"
})
public class InvestmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testProcessInvestment() throws Exception {
        mockMvc.perform(post("/api/investment/process/test-order-001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value("test-order-001"))
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/investment/health"))
            .andExpect(status().isOk());
    }
}
