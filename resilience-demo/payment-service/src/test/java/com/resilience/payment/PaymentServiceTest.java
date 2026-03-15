package com.resilience.payment;

import com.resilience.payment.controller.PaymentController;
import com.resilience.payment.model.PaymentResponse;
import com.resilience.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void testProcessPayment() throws Exception {
        String orderId = "order-789";
        PaymentResponse response = new PaymentResponse(
            orderId, "APPROVED", "Payment processed successfully", false, LocalDateTime.now()
        );
        when(paymentService.processPayment(orderId)).thenReturn(response);

        mockMvc.perform(post("/api/payment/process/{orderId}", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(orderId))
            .andExpect(jsonPath("$.status").value("APPROVED"))
            .andExpect(jsonPath("$.fallback").value(false));
    }

    @Test
    public void testProcessPaymentFallback() throws Exception {
        String orderId = "order-000";
        PaymentResponse fallbackResponse = new PaymentResponse(
            orderId, "DEGRADED", "Payment service operating in degraded mode", true, LocalDateTime.now()
        );
        when(paymentService.processPayment(orderId)).thenReturn(fallbackResponse);

        mockMvc.perform(post("/api/payment/process/{orderId}", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(orderId))
            .andExpect(jsonPath("$.status").value("DEGRADED"))
            .andExpect(jsonPath("$.fallback").value(true));
    }
}
