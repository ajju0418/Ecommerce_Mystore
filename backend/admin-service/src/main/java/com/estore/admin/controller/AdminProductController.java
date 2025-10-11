package com.estore.admin.controller;

import com.estore.admin.dto.ProductCategoryUpdateDto;
import com.estore.admin.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/admin/products")
@Validated
public class AdminProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);
    
    @Autowired
    private ProductManagementService productManagementService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts() {
        logger.info("Getting all products");
        Object products = productManagementService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Object> getProductsByCategory(@PathVariable String category) {
        logger.info("Getting products by category: {}", category != null ? category.replaceAll("[\r\n]", "") : "null");
        Object products = productManagementService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/update-category")
    public ResponseEntity<Object> updateProductCategory(@RequestBody @Validated ProductCategoryUpdateDto updateDto) {
        logger.info("Updating product category for product: {}", updateDto.getProductId() != null ? updateDto.getProductId().replaceAll("[\r\n]", "") : "null");
        Object result = productManagementService.updateProductCategory(updateDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable @NotNull Long productId) {
        logger.info("Deleting product: {}", productId);
        Object result = productManagementService.deleteProduct(productId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Object productDto) {
        logger.info("Creating new product");
        Object result = productManagementService.createProduct(productDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable @NotNull Long productId, @RequestBody Object productDto) {
        logger.info("Updating product: {}", productId);
        Object result = productManagementService.updateProduct(productId, productDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin Product Management Service is running");
    }
}