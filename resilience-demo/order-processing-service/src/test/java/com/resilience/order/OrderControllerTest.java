package com.resilience.order;

import com.resilience.order.controller.OrderController;
import com.resilience.order.model.OrderResponse;
import com.resilience.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void testProcessOrder() throws Exception {
        String orderId = "order-123";
        OrderResponse response = new OrderResponse(
            orderId, "PROCESSED", "Order processed successfully", "APPROVED", LocalDateTime.now()
        );
        when(orderService.processOrder(orderId)).thenReturn(response);

        mockMvc.perform(post("/api/orders/{orderId}", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(orderId))
            .andExpect(jsonPath("$.status").value("PROCESSED"));
    }

    @Test
    public void testProcessOrderFallback() throws Exception {
        String orderId = "order-456";
        OrderResponse fallbackResponse = new OrderResponse(
            orderId, "PENDING", "Payment service temporarily unavailable, order queued", "PENDING", LocalDateTime.now()
        );
        when(orderService.processOrder(orderId)).thenReturn(fallbackResponse);

        mockMvc.perform(post("/api/orders/{orderId}", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(orderId))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
