package com.estore.order.service;

import com.estore.order.client.UserServiceFeignClient;
import com.estore.order.dto.*;

import com.estore.order.entity.Order;
import com.estore.order.entity.Order.OrderStatus;
import com.estore.order.entity.OrderItem;
import com.estore.order.repository.OrderRepository;
import com.estore.order.repository.OrderItemRepository;
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
    private UserServiceFeignClient userServiceFeignClient;

    @Transactional
    public OrderDto createOrder(CheckoutDto checkoutDto) {
        // Generate unique order ID
        String orderId = generateOrderId();
        System.out.println("OrderService: Creating order with ID: " + orderId);
        
        // Calculate total amount
        BigDecimal totalAmount = checkoutDto.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        Order order = new Order(orderId, checkoutDto.getUserId(), totalAmount);
        
        // Set customer information if provided
        if (checkoutDto.getCustomerInfo() != null) {
            order.setCustomerName(checkoutDto.getCustomerInfo().getName());
            order.setCustomerEmail(checkoutDto.getCustomerInfo().getEmail());
            order.setCustomerPhone(checkoutDto.getCustomerInfo().getPhone());
            order.setCustomerAddress(checkoutDto.getCustomerInfo().getAddress());
        }
        
        System.out.println("OrderService: Saving order to database...");
        order = orderRepository.save(order);
        System.out.println("OrderService: Order saved with database ID: " + order.getId() + ", Order ID: " + order.getOrderId());
        
        // Create order items
        final Order finalOrder = order;
        List<OrderItem> orderItems = checkoutDto.getItems().stream()
                .map(item -> new OrderItem(finalOrder, item.getProductId(), item.getProductName(), 
                                         item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());
        
        System.out.println("OrderService: Saving " + orderItems.size() + " order items...");
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);
        System.out.println("OrderService: Order creation completed successfully");
        
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
        try {
            Object userObj = userServiceFeignClient.getUserById(order.getUserId());
            if (userObj instanceof UserResponseDto user) {
                orderDto.setUserName(user.getUsername());
                orderDto.setUserEmail(user.getEmail());
                orderDto.setUserPhone(user.getPhone());
            }
        } catch (Exception e) {
            System.out.println("User service call failed: " + e.getMessage());
        }
        return orderDto;
    }

    public List<OrderDto> getAllOrders() {
        System.out.println("OrderService: Fetching all orders from database...");
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        System.out.println("OrderService: Found " + orders.size() + " orders in database");
        for (Order order : orders) {
            System.out.println("OrderService: Order ID: " + order.getOrderId() + ", Status: " + order.getStatus() + ", Customer: " + order.getCustomerName());
        }
        return orders.stream()
                .map(order -> {
                    OrderDto orderDto = convertToDto(order);
                    // Try to get user information if customer info is missing
                    if (orderDto.getCustomerName() == null || orderDto.getCustomerName().isEmpty()) {
                        try {
                            Object userObj = userServiceFeignClient.getUserById(order.getUserId());
                            if (userObj instanceof UserResponseDto user) {
                                orderDto.setUserName(user.getUsername());
                                orderDto.setUserEmail(user.getEmail());
                                orderDto.setUserPhone(user.getPhone());
                                System.out.println("OrderService: Fetched user info for order " + order.getOrderId() + ": " + user.getUsername());
                            }
                        } catch (Exception e) {
                            System.out.println("OrderService: User service call failed for order " + order.getOrderId() + ": " + e.getMessage());
                        }
                    }
                    return orderDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto updateOrderStatus(String orderId, OrderStatus status) {
        System.out.println("OrderService: Looking for order with ID: " + orderId);
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> {
                    System.out.println("OrderService: Order not found with ID: " + orderId);
                    return new RuntimeException("Order not found with ID: " + orderId);
                });
        
        System.out.println("OrderService: Found order, updating status from " + order.getStatus() + " to " + status);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        System.out.println("OrderService: Order status updated successfully");
        
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
                order.setStatus(OrderStatus.PROCESSING);
            }
        }
        
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        
        return convertToDto(order);
    }
    
    private String generateOrderId() {
        return String.valueOf(100000 + new java.util.Random().nextInt(900000));
    }

    private OrderDto convertToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getOrderItems() != null ? 
                order.getOrderItems().stream()
                        .map(item -> new OrderItemDto(item.getId(), item.getProductId(), 
                                                    item.getProductName(), item.getPrice(), 
                                                    item.getQuantity(), item.getSubtotal()))
                        .collect(Collectors.toList()) : List.of();
        
        OrderDto orderDto = new OrderDto(order.getId(), order.getOrderId(), order.getUserId(), 
                           order.getTotalAmount(), order.getStatus(), order.getCreatedAt(), 
                           order.getUpdatedAt(), itemDtos);
        
        // Set customer information
        orderDto.setCustomerName(order.getCustomerName());
        orderDto.setCustomerEmail(order.getCustomerEmail());
        orderDto.setCustomerPhone(order.getCustomerPhone());
        orderDto.setCustomerAddress(order.getCustomerAddress());
        
        return orderDto;
    }
}