package com.estore.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order status update request")
public class OrderStatusUpdateDto {
    @NotBlank(message = "Order ID is required")
    @Schema(description = "Order ID", example = "ORD123")
    private String orderId;
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(PENDING|PROCESSING|COMPLETED|CANCELLED)$", message = "Invalid status")
    @Schema(description = "Order status", example = "COMPLETED", allowableValues = {"PENDING", "PROCESSING", "COMPLETED", "CANCELLED"})
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