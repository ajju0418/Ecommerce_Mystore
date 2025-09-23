package com.estore.admin.dto;

public class ProductCategoryUpdateDto {
    private String productId;
    private String category;

    public ProductCategoryUpdateDto() {}

    public ProductCategoryUpdateDto(String productId, String category) {
        this.productId = productId;
        this.category = category;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}