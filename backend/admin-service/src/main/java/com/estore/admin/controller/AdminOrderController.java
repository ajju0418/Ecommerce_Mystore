package com.estore.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.estore.admin.dto.OrderStatusUpdateDto;
import com.estore.admin.service.OrderManagementService;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/admin/orders")
@Validated
public class AdminOrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);
    
    @Autowired
    private OrderManagementService orderManagementService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOrders() {
        logger.info("Getting all orders");
        Object orders = orderManagementService.getAllOrders();
        logger.info("Successfully retrieved orders");
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable @NotBlank String orderId) {
        logger.info("Getting order by ID: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object order = orderManagementService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getUserOrders(@PathVariable Long userId) {
        logger.info("Getting orders for user: {}", userId);
        Object orders = orderManagementService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/update-status")
    public ResponseEntity<Object> updateOrderStatus(@RequestBody @Validated OrderStatusUpdateDto updateDto) {
        logger.info("Updating order status for order: {}", updateDto.getOrderId() != null ? updateDto.getOrderId().replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.updateOrderStatus(updateDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin Order Management Service is running");
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Admin service test endpoint working");
    }
    
    @PutMapping("/accept/{orderId}")
    public ResponseEntity<Object> acceptOrder(@PathVariable @NotBlank String orderId) {
        logger.info("Accepting order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.acceptOrder(orderId);
        return ResponseEntity.ok(result);
    }
    

    
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable @NotBlank String orderId) {
        logger.info("Deleting order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.deleteOrder(orderId);
        logger.info("Successfully deleted order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/mark-completed/{orderId}")
    public ResponseEntity<Object> markOrderAsCompleted(@PathVariable @NotBlank String orderId) {
        logger.info("Marking order as completed: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.markOrderAsCompleted(orderId);
        logger.info("Successfully marked order as completed: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        return ResponseEntity.ok(result);
    }
}