package com.estore.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback controller for circuit breaker patterns
 * Provides fallback responses when services are unavailable
 */
@RestController
public class FallbackController {

    private static final Logger logger = LoggerFactory.getLogger(FallbackController.class);

    @RequestMapping("/fallback/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        logger.warn("User service fallback triggered - service may be down");
        return createFallbackResponse("User Service");
    }

    @RequestMapping("/fallback/product-service")
    public ResponseEntity<Map<String, Object>> productServiceFallback() {
        logger.warn("Product service fallback triggered - service may be down");
        return createFallbackResponse("Product Service");
    }

    @RequestMapping("/fallback/cart-service")
    public ResponseEntity<Map<String, Object>> cartServiceFallback() {
        logger.warn("Cart service fallback triggered - service may be down");
        return createFallbackResponse("Cart Service");
    }

    @RequestMapping("/fallback/order-service")
    public ResponseEntity<Map<String, Object>> orderServiceFallback() {
        logger.warn("Order service fallback triggered - service may be down");
        return createFallbackResponse("Order Service");
    }

    @RequestMapping("/fallback/payment-service")
    public ResponseEntity<Map<String, Object>> paymentServiceFallback() {
        logger.warn("Payment service fallback triggered - service may be down");
        return createFallbackResponse("Payment Service");
    }

    @RequestMapping("/fallback/admin-service")
    public ResponseEntity<Map<String, Object>> adminServiceFallback() {
        logger.warn("Admin service fallback triggered - service may be down");
        return createFallbackResponse("Admin Service");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String serviceName) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Service Unavailable");
        response.put("message", serviceName + " is currently unavailable. Please try again later.");
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}