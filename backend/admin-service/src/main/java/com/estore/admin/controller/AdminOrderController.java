package com.estore.admin.controller;

import com.estore.admin.dto.OrderStatusUpdateDto;
import com.estore.admin.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
    
    @Autowired
    private OrderManagementService orderManagementService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOrders() {
        try {
            System.out.println("AdminOrderController: Getting all orders...");
            Object orders = orderManagementService.getAllOrders();
            System.out.println("AdminOrderController: Successfully retrieved orders");
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.out.println("AdminOrderController: Error getting orders: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable String orderId) {
        try {
            Object order = orderManagementService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getUserOrders(@PathVariable Long userId) {
        try {
            Object orders = orderManagementService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<Object> updateOrderStatus(@RequestBody OrderStatusUpdateDto updateDto) {
        try {
            Object result = orderManagementService.updateOrderStatus(updateDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
    public ResponseEntity<Object> acceptOrder(@PathVariable String orderId) {
        try {
            Object result = orderManagementService.acceptOrder(orderId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/remove/{orderId}")
    public ResponseEntity<Object> removeOrder(@PathVariable String orderId) {
        try {
            Object result = orderManagementService.removeOrder(orderId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable String orderId) {
        try {
            System.out.println("AdminOrderController: Deleting order: " + orderId);
            Object result = orderManagementService.deleteOrder(orderId);
            System.out.println("AdminOrderController: Successfully deleted order");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("AdminOrderController: Error deleting order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/mark-delivered/{orderId}")
    public ResponseEntity<Object> markOrderAsDelivered(@PathVariable String orderId) {
        try {
            System.out.println("AdminOrderController: Marking order as delivered: " + orderId);
            Object result = orderManagementService.markOrderAsDelivered(orderId);
            System.out.println("AdminOrderController: Successfully marked order as delivered");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("AdminOrderController: Error marking order as delivered: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}