package com.estore.order.dto;

import java.util.List;

public class CheckoutDto {
    private Long userId;
    private List<CheckoutItemDto> items;
    private CustomerInfoDto customerInfo;

    public CheckoutDto() {}

    public CheckoutDto(Long userId, List<CheckoutItemDto> items) {
        this.userId = userId;
        this.items = items;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public List<CheckoutItemDto> getItems() { return items; }
    public void setItems(List<CheckoutItemDto> items) { this.items = items; }
    
    public CustomerInfoDto getCustomerInfo() { return customerInfo; }
    public void setCustomerInfo(CustomerInfoDto customerInfo) { this.customerInfo = customerInfo; }
}