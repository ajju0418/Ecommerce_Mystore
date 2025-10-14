package com.estore.order.service;

import com.estore.order.client.UserServiceFeignClient;
import com.estore.order.dto.*;
import com.estore.order.entity.Order;
import com.estore.order.repository.OrderRepository;
import com.estore.order.repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserServiceFeignClient userServiceFeignClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ShouldReturnOrderDto() {
        // Given
        CheckoutDto checkoutDto = new CheckoutDto();
        checkoutDto.setUserId(1L);
        
        CheckoutItemDto item = new CheckoutItemDto();
        item.setProductId("1");
        item.setProductName("Test Product");
        item.setPrice(BigDecimal.valueOf(29.99));
        item.setQuantity(2);
        checkoutDto.setItems(Arrays.asList(item));

        Order savedOrder = new Order("123456", 1L, BigDecimal.valueOf(59.98));
        savedOrder.setId(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        OrderDto result = orderService.createOrder(checkoutDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderByOrderId_WhenExists_ShouldReturnOrderDto() {
        // Given
        String orderId = "123456";
        Order order = new Order(orderId, 1L, BigDecimal.valueOf(29.99));
        order.setId(1L);
        order.setOrderItems(Arrays.asList());
        
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));

        // When
        OrderDto result = orderService.getOrderByOrderId(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void getAllOrders_ShouldReturnOrderList() {
        // Given
        Order order1 = new Order("123456", 1L, BigDecimal.valueOf(29.99));
        order1.setOrderItems(Arrays.asList());
        
        List<Order> orders = Arrays.asList(order1);
        when(orderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(orders);

        // When
        List<OrderDto> result = orderService.getAllOrders();

        // Then
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }
}