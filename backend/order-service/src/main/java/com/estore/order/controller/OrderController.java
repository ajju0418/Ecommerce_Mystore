package com.estore.order.controller;

import com.estore.order.dto.CheckoutDto;
import com.estore.order.dto.OrderDto;
import com.estore.order.entity.Order.OrderStatus;
import com.estore.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CheckoutDto checkoutDto) {
        try {
            OrderDto order = orderService.createOrder(checkoutDto);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrderAlternate(@RequestBody CheckoutDto checkoutDto) {
        try {
            OrderDto order = orderService.createOrder(checkoutDto);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrderHistory(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = orderService.getUserOrderHistory(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderByOrderId(@PathVariable String orderId) {
        try {
            OrderDto order = orderService.getOrderByOrderId(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        try {
            List<OrderDto> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> body) {
        try {
            OrderStatus status = OrderStatus.valueOf(body.get("status"));
            OrderDto order = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable String orderId) {
        try {
            OrderDto order = orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create-with-payment")
    public ResponseEntity<OrderDto> createOrderWithPayment(@RequestBody CheckoutDto checkoutDto) {
        try {
            OrderDto order = orderService.createOrder(checkoutDto);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Service is running");
    }
        @GetMapping("/orderdetails/{orderId}")
        public ResponseEntity<OrderDto> getOrderDetails(@PathVariable String orderId) {
            try {
                OrderDto order = orderService.getOrderByOrderId(orderId);
                return ResponseEntity.ok(order);
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        }
        
        @DeleteMapping("/{orderId}")
        public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
            try {
                System.out.println("OrderController: Attempting to delete order: " + orderId);
                orderService.deleteOrder(orderId);
                System.out.println("OrderController: Order deleted successfully");
                return ResponseEntity.ok("Order deleted successfully");
            } catch (Exception e) {
                System.out.println("OrderController: Delete failed: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Failed to delete order: " + e.getMessage());
            }
        }
        
    @GetMapping("/check/{orderId}")
    public ResponseEntity<String> checkOrder(@PathVariable String orderId) {
        try {
            OrderDto order = orderService.getOrderByOrderId(orderId);
            return ResponseEntity.ok("Order exists: " + order.getOrderId());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{orderId}/payment")
    public ResponseEntity<OrderDto> updateOrderPayment(@PathVariable String orderId, @RequestBody Map<String, Object> paymentDetails) {
        try {
            OrderDto order = orderService.updateOrderPayment(orderId, paymentDetails);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}