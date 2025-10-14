package com.estore.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.estore.admin.dto.OrderStatusUpdateDto;
import com.estore.admin.service.OrderManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/admin/orders")
@Validated
@Tag(name = "Admin Order Management")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "500", description = "Server error")
})
public class AdminOrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);
    
    @Autowired
    private OrderManagementService orderManagementService;

    @GetMapping("/all")
    @Operation(summary = "Get all orders")
    public ResponseEntity<Object> getAllOrders() {
        logger.info("Getting all orders");
        Object orders = orderManagementService.getAllOrders();
        logger.info("Successfully retrieved orders");
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Object> getOrderById(
            @Parameter(example = "ORD123") @PathVariable @NotBlank String orderId) {
        logger.info("Getting order by ID: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object order = orderManagementService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user ID")
    public ResponseEntity<Object> getUserOrders(
            @Parameter(example = "12345") @PathVariable Long userId) {
        logger.info("Getting orders for user: {}", userId);
        Object orders = orderManagementService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/update-status")
    @Operation(summary = "Update order status")
    @ApiResponse(responseCode = "400", description = "Invalid status")
    public ResponseEntity<Object> updateOrderStatus(@RequestBody @Validated OrderStatusUpdateDto updateDto) {
        logger.info("Updating order status for order: {}", updateDto.getOrderId() != null ? updateDto.getOrderId().replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.updateOrderStatus(updateDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin Order Management Service is running");
    }
    
    @GetMapping("/test")
    @Operation(summary = "Test endpoint")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Admin service test endpoint working");
    }
    
    @PutMapping("/accept/{orderId}")
    @Operation(summary = "Accept order")
    public ResponseEntity<Object> acceptOrder(
            @Parameter(example = "ORD123") @PathVariable @NotBlank String orderId) {
        logger.info("Accepting order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.acceptOrder(orderId);
        return ResponseEntity.ok(result);
    }
    

    
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Object> deleteOrder(
            @Parameter(example = "ORD123") @PathVariable @NotBlank String orderId) {
        logger.info("Deleting order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.deleteOrder(orderId);
        logger.info("Successfully deleted order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/mark-completed/{orderId}")
    @Operation(summary = "Mark order as completed")
    public ResponseEntity<Object> markOrderAsCompleted(
            @Parameter(example = "ORD123") @PathVariable @NotBlank String orderId) {
        logger.info("Marking order as completed: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        Object result = orderManagementService.markOrderAsCompleted(orderId);
        logger.info("Successfully marked order as completed: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        return ResponseEntity.ok(result);
    }
}