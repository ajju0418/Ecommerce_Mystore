package com.estore.cart.dto;

import java.util.List;

public class CartResponseDto {
    private Long cartId;
    private Long userId;
    private List<CartItemDto> items;
    private Integer totalItems;
    private Double totalAmount;

    public CartResponseDto() {}

    public CartResponseDto(Long cartId, Long userId, List<CartItemDto> items, Double totalAmount) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.totalItems = items.stream().mapToInt(CartItemDto::getQuantity).sum();
        this.totalAmount = totalAmount;
    }

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }
    
    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}