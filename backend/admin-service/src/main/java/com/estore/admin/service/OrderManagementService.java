package com.estore.admin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estore.admin.dto.OrderStatusUpdateDto;

@Service
public class OrderManagementService {
    
    @Autowired
    private RestTemplate restTemplate;

    public Object getAllOrders() {
        return restTemplate.getForObject("http://order-service/api/orders/all", Object.class);
    }

    public Object getOrderById(String orderId) {
        return restTemplate.getForObject("http://order-service/api/orders/" + orderId, Object.class);
    }

    public Object getUserOrders(Long userId) {
        return restTemplate.getForObject("http://order-service/api/orders/user/" + userId, Object.class);
    }

    public Object updateOrderStatus(OrderStatusUpdateDto updateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("status", updateDto.getStatus());
        
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        
        String url = "http://order-service/api/orders/" + updateDto.getOrderId() + "/status";
        return restTemplate.exchange(url, HttpMethod.PUT, request, Object.class).getBody();
    }
    
    public Object deleteOrder(String orderId) {
        try {
            System.out.println("OrderManagementService: Deleting order via: http://order-service/api/orders/" + orderId);
            String url = "http://order-service/api/orders/" + orderId;
            Object result = restTemplate.exchange(url, HttpMethod.DELETE, null, Object.class).getBody();
            System.out.println("OrderManagementService: Delete successful");
            return result;
        } catch (Exception e) {
            System.out.println("OrderManagementService: Delete failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Object acceptOrder(String orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("status", "PROCESSING");
        
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        
        String url = "http://order-service/api/orders/" + orderId + "/status";
        return restTemplate.exchange(url, HttpMethod.PUT, request, Object.class).getBody();
    }

    public Object removeOrder(String orderId) {
        String url = "http://order-service/api/orders/" + orderId;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Object.class).getBody();
    }
    
    public Object markOrderAsCompleted(String orderId) {
        try {
            System.out.println("OrderManagementService: Marking order as completed: " + orderId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("status", "COMPLETED");
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            String url = "http://order-service/api/orders/" + orderId + "/status";
            Object result = restTemplate.exchange(url, HttpMethod.PUT, request, Object.class).getBody();
            
            System.out.println("OrderManagementService: Successfully marked order as completed");
            return result;
        } catch (Exception e) {
            System.out.println("OrderManagementService: Failed to mark order as completed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}