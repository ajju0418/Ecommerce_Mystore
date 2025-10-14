package com.estore.order.controller;

import com.estore.order.dto.OrderDto;
import com.estore.order.entity.Order;
import com.estore.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = OrderController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void getAllOrders_ShouldReturnOrderList() throws Exception {
        // Given
        OrderDto order1 = new OrderDto(1L, "123456", 1L, BigDecimal.valueOf(29.99), 
                                      Order.OrderStatus.PENDING, LocalDateTime.now(), 
                                      LocalDateTime.now(), Arrays.asList());
        List<OrderDto> orders = Arrays.asList(order1);
        when(orderService.getAllOrders()).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].orderId").value("123456"));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() throws Exception {
        // Given
        OrderDto order = new OrderDto(1L, "123456", 1L, BigDecimal.valueOf(29.99), 
                                     Order.OrderStatus.PENDING, LocalDateTime.now(), 
                                     LocalDateTime.now(), Arrays.asList());
        when(orderService.getOrderByOrderId("123456")).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/api/orders/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("123456"))
                .andExpect(jsonPath("$.userId").value(1));

        verify(orderService, times(1)).getOrderByOrderId("123456");
    }

    @Test
    void getUserOrders_ShouldReturnUserOrderList() throws Exception {
        // Given
        OrderDto order = new OrderDto(1L, "123456", 1L, BigDecimal.valueOf(29.99), 
                                     Order.OrderStatus.PENDING, LocalDateTime.now(), 
                                     LocalDateTime.now(), Arrays.asList());
        List<OrderDto> orders = Arrays.asList(order);
        when(orderService.getUserOrderHistory(1L)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(1));

        verify(orderService, times(1)).getUserOrderHistory(1L);
    }
}