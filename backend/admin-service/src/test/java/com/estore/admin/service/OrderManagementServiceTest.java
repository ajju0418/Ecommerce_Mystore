package com.estore.admin.service;

import com.estore.admin.client.OrderServiceFeignClient;
import com.estore.admin.dto.OrderStatusUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderManagementServiceTest {

    @Mock
    private OrderServiceFeignClient orderServiceClient;

    @InjectMocks
    private OrderManagementService orderManagementService;

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        // Given
        List<Object> mockOrders = Arrays.asList("order1", "order2");
        when(orderServiceClient.getAllOrders()).thenReturn(mockOrders);

        // When
        Object result = orderManagementService.getAllOrders();

        // Then
        assertEquals(mockOrders, result);
        verify(orderServiceClient, times(1)).getAllOrders();
    }

    @Test
    void getOrderById_WithValidId_ShouldReturnOrder() {
        // Given
        String orderId = "123";
        Object mockOrder = "orderData";
        when(orderServiceClient.getOrderById(123L)).thenReturn(mockOrder);

        // When
        Object result = orderManagementService.getOrderById(orderId);

        // Then
        assertEquals(mockOrder, result);
        verify(orderServiceClient, times(1)).getOrderById(123L);
    }

    @Test
    void getOrderById_WithInvalidId_ShouldThrowException() {
        // Given
        String invalidId = "invalid";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> orderManagementService.getOrderById(invalidId));
        
        assertTrue(exception.getMessage().contains("Invalid order ID format"));
    }

    @Test
    void updateOrderStatus_ShouldCallOrderService() {
        // Given
        OrderStatusUpdateDto updateDto = new OrderStatusUpdateDto();
        updateDto.setOrderId("123");
        updateDto.setStatus("PROCESSING");
        
        Object mockResponse = "updated";
        when(orderServiceClient.updateOrderStatus("123", updateDto)).thenReturn(mockResponse);

        // When
        Object result = orderManagementService.updateOrderStatus(updateDto);

        // Then
        assertEquals(mockResponse, result);
        verify(orderServiceClient, times(1)).updateOrderStatus("123", updateDto);
    }

    @Test
    void acceptOrder_ShouldUpdateStatusToProcessing() {
        // Given
        String orderId = "123";
        Object mockResponse = "accepted";
        when(orderServiceClient.updateOrderStatus(eq(orderId), any(OrderStatusUpdateDto.class)))
            .thenReturn(mockResponse);

        // When
        Object result = orderManagementService.acceptOrder(orderId);

        // Then
        assertEquals(mockResponse, result);
        verify(orderServiceClient, times(1)).updateOrderStatus(eq(orderId), any(OrderStatusUpdateDto.class));
    }

    @Test
    void deleteOrder_WithValidId_ShouldCallOrderService() {
        // Given
        String orderId = "123";
        Object mockResponse = "deleted";
        when(orderServiceClient.deleteOrder(123L)).thenReturn(mockResponse);

        // When
        Object result = orderManagementService.deleteOrder(orderId);

        // Then
        assertEquals(mockResponse, result);
        verify(orderServiceClient, times(1)).deleteOrder(123L);
    }
}