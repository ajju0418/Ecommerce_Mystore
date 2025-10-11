package com.estore.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OrderStatusUpdateDto {
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(PENDING|PROCESSING|COMPLETED|CANCELLED)$", message = "Invalid status")
    private String status;

    public OrderStatusUpdateDto() {}

    public OrderStatusUpdateDto(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}