package com.estore.order.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.order.dto.CheckoutDto;
import com.estore.order.dto.OrderDto;
import com.estore.order.entity.Order.OrderStatus;
import com.estore.order.service.OrderService;

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
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> body) {
        try {
            System.out.println("=== ORDER STATUS UPDATE DEBUG ===");
            System.out.println("OrderController: Updating order status for orderId: " + orderId);
            System.out.println("OrderController: Request body: " + body);
            System.out.println("OrderController: Request body type: " + (body != null ? body.getClass().getSimpleName() : "null"));
            
            // Validate request body
            if (body == null || body.isEmpty()) {
                System.out.println("OrderController: Empty request body - returning 400");
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Request body is required"));
            }
            
            String statusStr = body.get("status");
            if (statusStr == null || statusStr.isEmpty()) {
                System.out.println("OrderController: Status is missing from request body - returning 400");
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Status is required"));
            }
            
            System.out.println("OrderController: Status string: " + statusStr);
            
            // Validate status value
            OrderStatus status;
            try {
                status = OrderStatus.valueOf(statusStr.toUpperCase());
                System.out.println("OrderController: Parsed status: " + status);
            } catch (IllegalArgumentException e) {
                System.out.println("OrderController: Invalid status value: " + statusStr + " - returning 400");
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid status: " + statusStr + ". Valid statuses: PENDING, PROCESSING, COMPLETED, CANCELLED"));
            }
            
            // Check if order exists first
            try {
                System.out.println("OrderController: Checking if order exists: " + orderId);
                OrderDto existingOrder = orderService.getOrderByOrderId(orderId);
                System.out.println("OrderController: Found existing order: " + existingOrder.getOrderId());
            } catch (RuntimeException e) {
                System.out.println("OrderController: Order not found: " + orderId + " - returning 404");
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "Order not found with ID: " + orderId));
            }
            
            System.out.println("OrderController: Proceeding with status update...");
            OrderDto order = orderService.updateOrderStatus(orderId, status);
            System.out.println("OrderController: Order updated successfully");
            
            // Return success response with order data
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Order status updated successfully",
                "order", order
            );
            System.out.println("OrderController: Returning success response");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("OrderController: RuntimeException during update: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Error: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println("OrderController: Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "Unexpected error: " + e.getMessage()));
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
    
    @GetMapping("/debug/list")
    public ResponseEntity<String> debugListOrders() {
        try {
            List<OrderDto> orders = orderService.getAllOrders();
            StringBuilder sb = new StringBuilder("Orders in database:\n");
            for (OrderDto order : orders) {
                sb.append("OrderID: ").append(order.getOrderId())
                  .append(", DB_ID: ").append(order.getId())
                  .append(", Status: ").append(order.getStatus())
                  .append(", Amount: ").append(order.getTotalAmount())
                  .append("\n");
            }
            return ResponseEntity.ok(sb.toString());
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/debug/find/{orderId}")
    public ResponseEntity<String> debugFindOrder(@PathVariable String orderId) {
        try {
            System.out.println("Debug: Looking for order with ID: " + orderId);
            OrderDto order = orderService.getOrderByOrderId(orderId);
            String result = "Found order: " + order.getOrderId() + ", Status: " + order.getStatus();
            System.out.println("Debug: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String error = "Order not found: " + orderId + ", Error: " + e.getMessage();
            System.out.println("Debug: " + error);
            return ResponseEntity.ok(error);
        }
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