package com.estore.admin.dto;

public class OrderStatusUpdateDto {
    private String orderId;
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