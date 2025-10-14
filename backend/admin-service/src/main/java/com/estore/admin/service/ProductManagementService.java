package com.estore.admin.service;

import com.estore.admin.dto.ProductCategoryUpdateDto;
import com.estore.admin.client.ProductServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductManagementService {
    
    @Autowired
    private ProductServiceFeignClient productServiceClient;

    public Object getAllProducts() {
        return productServiceClient.getAllProducts();
    }

    public Object getProductsByCategory(String category) {
        return productServiceClient.getProductsByCategory(category);
    }

    public Object updateProductCategory(ProductCategoryUpdateDto updateDto) {
        if (updateDto.getProductId() == null || updateDto.getProductId().trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        try {
            Long productId = Long.parseLong(updateDto.getProductId());
            return productServiceClient.updateProduct(productId, updateDto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid product ID format: " + updateDto.getProductId());
        }
    }

    public Object deleteProduct(Long productId) {
        return productServiceClient.deleteProduct(productId);
    }

    public Object createProduct(Object productDto) {
        return productServiceClient.createProduct(productDto);
    }

    public Object updateProduct(Long productId, Object productDto) {
        return productServiceClient.updateProduct(productId, productDto);
    }


}