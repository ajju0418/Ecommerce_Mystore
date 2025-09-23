package com.estore.payment.controller;

import com.estore.payment.dto.PaymentRequest;
import com.estore.payment.dto.PaymentResponse;
import com.estore.payment.entity.Payment;
import com.estore.payment.entity.PaymentStatus;
import com.estore.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = paymentService.processPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Payment processing failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionId(@PathVariable String transactionId) {
        try {
            PaymentResponse response = paymentService.getPaymentByTransactionId(transactionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        try {
            PaymentResponse response = paymentService.getPaymentByOrderId(orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable Long userId) {
        try {
            List<Payment> payments = paymentService.getPaymentsByUserId(userId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        try {
            List<Payment> payments = paymentService.getAllPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/status/{transactionId}")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable String transactionId,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            PaymentStatus status = PaymentStatus.valueOf(statusUpdate.get("status"));
            PaymentResponse response = paymentService.updatePaymentStatus(transactionId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/verify/{transactionId}")
    public ResponseEntity<Map<String, Boolean>> verifyPayment(@PathVariable String transactionId) {
        try {
            boolean isVerified = paymentService.verifyPayment(transactionId);
            return ResponseEntity.ok(Map.of("verified", isVerified));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("verified", false));
        }
    }
    
    @GetMapping("/stats/total-payments")
    public ResponseEntity<Map<String, Long>> getTotalPayments() {
        try {
            Long totalPayments = paymentService.getTotalCompletedPayments();
            return ResponseEntity.ok(Map.of("totalPayments", totalPayments));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("totalPayments", 0L));
        }
    }
    
    @GetMapping("/stats/total-revenue")
    public ResponseEntity<Map<String, Double>> getTotalRevenue() {
        try {
            Double totalRevenue = paymentService.getTotalRevenue();
            return ResponseEntity.ok(Map.of("totalRevenue", totalRevenue != null ? totalRevenue : 0.0));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("totalRevenue", 0.0));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Payment Service",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}