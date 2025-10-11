package com.estore.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.estore.admin.client.OrderServiceFeignClient;
import com.estore.admin.dto.OrderStatusUpdateDto;

@Service
public class OrderManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);
    
    @Autowired
    private OrderServiceFeignClient orderServiceClient;

    public Object getAllOrders() {
        return orderServiceClient.getAllOrders();
    }

    public Object getOrderById(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        try {
            return orderServiceClient.getOrderById(Long.parseLong(orderId));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid order ID format: " + orderId);
        }
    }

    public Object getUserOrders(Long userId) {
        return orderServiceClient.getUserOrders(userId);
    }

    public Object updateOrderStatus(OrderStatusUpdateDto updateDto) {
        return orderServiceClient.updateOrderStatus(updateDto.getOrderId(), updateDto);
    }
    
    public Object deleteOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        try {
            logger.info("Deleting order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
            Object result = orderServiceClient.deleteOrder(Long.parseLong(orderId));
            logger.info("Delete successful for order: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
            return result;
        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format provided");
            throw new IllegalArgumentException("Invalid order ID format: " + orderId);
        }
    }

    public Object acceptOrder(String orderId) {
        OrderStatusUpdateDto updateDto = new OrderStatusUpdateDto();
        updateDto.setOrderId(orderId);
        updateDto.setStatus("PROCESSING");
        return orderServiceClient.updateOrderStatus(orderId, updateDto);
    }


    
    public Object markOrderAsCompleted(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        logger.info("Marking order as completed: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        OrderStatusUpdateDto updateDto = new OrderStatusUpdateDto();
        updateDto.setOrderId(orderId);
        updateDto.setStatus("COMPLETED");
        Object result = orderServiceClient.updateOrderStatus(orderId, updateDto);
        logger.info("Successfully marked order as completed: {}", orderId != null ? orderId.replaceAll("[\r\n]", "") : "null");
        return result;
    }

}