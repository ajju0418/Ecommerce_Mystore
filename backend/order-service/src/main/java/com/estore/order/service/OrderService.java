package com.estore.order.service;

import com.estore.order.dto.*;

import com.estore.order.entity.Order;
import com.estore.order.entity.Order.OrderStatus;
import com.estore.order.entity.OrderItem;
import com.estore.order.repository.OrderRepository;
import com.estore.order.repository.OrderItemRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    




    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public OrderDto createOrder(CheckoutDto checkoutDto) {
        // Generate unique order ID
        String orderId = generateOrderId();
        
        // Calculate total amount
        BigDecimal totalAmount = checkoutDto.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        Order order = new Order(orderId, checkoutDto.getUserId(), totalAmount);
        order = orderRepository.save(order);
        
        // Create order items
        final Order finalOrder = order;
        List<OrderItem> orderItems = checkoutDto.getItems().stream()
                .map(item -> new OrderItem(finalOrder, item.getProductId(), item.getProductName(), 
                                         item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());
        
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);
        
        return convertToDto(order);
    }

    public List<OrderDto> getUserOrderHistory(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        OrderDto orderDto = convertToDto(order);
        
        // Add user details via HTTP call to user service
        try {
            String userServiceUrl = "http://localhost:9091/api/users/" + order.getUserId();
            System.out.println("Calling user service: " + userServiceUrl);
            UserResponseDto user = restTemplate.getForObject(userServiceUrl, UserResponseDto.class);
            if (user != null) {
                System.out.println("User found: " + user.getUsername());
                orderDto.setUserName(user.getUsername());
                orderDto.setUserEmail(user.getEmail());
                orderDto.setUserPhone(user.getPhone());
            } else {
                System.out.println("User not found");
            }
        } catch (RestClientException e) {
            System.out.println("User service call failed: " + e.getMessage());
        }
        
        return orderDto;
    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        
        return convertToDto(order);
    }

    @Transactional
    public void deleteOrder(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        // Delete order items first
        orderItemRepository.deleteAll(order.getOrderItems());
        
        // Delete the order
        orderRepository.delete(order);
    }

    @Transactional
    public OrderDto updateOrderPayment(String orderId, java.util.Map<String, Object> paymentDetails) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        // Update order with payment information
        // You can add payment-related fields to the Order entity if needed
        // For now, we'll just update the status to indicate payment is processed
        if (paymentDetails.containsKey("status")) {
            String paymentStatus = (String) paymentDetails.get("status");
            if ("completed".equals(paymentStatus) || "success".equals(paymentStatus)) {
                order.setStatus(OrderStatus.PAID);
            }
        }
        
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        
        return convertToDto(order);
    }
    
    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderDto convertToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getOrderItems() != null ? 
                order.getOrderItems().stream()
                        .map(item -> new OrderItemDto(item.getId(), item.getProductId(), 
                                                    item.getProductName(), item.getPrice(), 
                                                    item.getQuantity(), item.getSubtotal()))
                        .collect(Collectors.toList()) : List.of();
        
        return new OrderDto(order.getId(), order.getOrderId(), order.getUserId(), 
                           order.getTotalAmount(), order.getStatus(), order.getCreatedAt(), 
                           order.getUpdatedAt(), itemDtos);
    }
}