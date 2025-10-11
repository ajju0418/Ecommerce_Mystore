package com.estore.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gateway management controller providing health checks and service discovery information
 */
@RestController
@RequestMapping("/gateway")
@CrossOrigin(origins = "*")
public class GatewayController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getGatewayHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "API Gateway");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }

    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> getRegisteredServices() {
        Map<String, Object> services = new HashMap<>();
        
        try {
            List<String> serviceNames = discoveryClient.getServices();
            Map<String, Object> serviceDetails = new HashMap<>();
            
            for (String serviceName : serviceNames) {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                Map<String, Object> serviceInfo = new HashMap<>();
                serviceInfo.put("instanceCount", instances.size());
                serviceInfo.put("instances", instances);
                serviceDetails.put(serviceName, serviceInfo);
            }
            
            services.put("status", "UP");
            services.put("totalServices", serviceNames.size());
            services.put("services", serviceDetails);
            services.put("timestamp", LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("Error retrieving registered services", e);
            services.put("status", "ERROR");
            services.put("error", "Failed to retrieve services: " + e.getMessage());
            services.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(500).body(services);
        }
        
        return ResponseEntity.ok(services);
    }

    @GetMapping("/routes")
    public ResponseEntity<Map<String, Object>> getRoutes() {
        Map<String, Object> routes = new HashMap<>();
        
        // Static route information
        Map<String, String> routeMap = new HashMap<>();
        routeMap.put("/api/users/**", "user-service");
        routeMap.put("/api/products/**", "product-service");
        routeMap.put("/api/cart/**", "cart-service");
        routeMap.put("/api/orders/**", "order-service");
        routeMap.put("/api/payments/**", "payment-service");
        routeMap.put("/api/admin/**", "admin-service");
        
        routes.put("status", "UP");
        routes.put("routes", routeMap);
        routes.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/service/{serviceName}")
    public ResponseEntity<Map<String, Object>> getServiceInfo(@PathVariable String serviceName) {
        Map<String, Object> serviceInfo = new HashMap<>();
        
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            
            if (instances.isEmpty()) {
                serviceInfo.put("status", "NOT_FOUND");
                serviceInfo.put("message", "Service not found or not registered");
                return ResponseEntity.notFound().build();
            }
            
            serviceInfo.put("status", "UP");
            serviceInfo.put("serviceName", serviceName);
            serviceInfo.put("instanceCount", instances.size());
            serviceInfo.put("instances", instances);
            serviceInfo.put("timestamp", LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("Error retrieving service info for: {}", serviceName, e);
            serviceInfo.put("status", "ERROR");
            serviceInfo.put("error", "Failed to retrieve service info: " + e.getMessage());
            serviceInfo.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(500).body(serviceInfo);
        }
        
        return ResponseEntity.ok(serviceInfo);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getGatewayStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<String> services = discoveryClient.getServices();
            int totalInstances = 0;
            
            for (String service : services) {
                totalInstances += discoveryClient.getInstances(service).size();
            }
            
            stats.put("status", "UP");
            stats.put("totalServices", services.size());
            stats.put("totalInstances", totalInstances);
            stats.put("gatewayUptime", "Running");
            stats.put("timestamp", LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("Error retrieving gateway statistics", e);
            stats.put("status", "ERROR");
            stats.put("error", "Failed to retrieve statistics: " + e.getMessage());
            stats.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(500).body(stats);
        }
        
        return ResponseEntity.ok(stats);
    }
}