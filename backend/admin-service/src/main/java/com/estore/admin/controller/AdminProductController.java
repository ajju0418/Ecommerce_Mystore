package com.estore.admin.controller;

import com.estore.admin.dto.ProductCategoryUpdateDto;
import com.estore.admin.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    
    @Autowired
    private ProductManagementService productManagementService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts() {
        try {
            Object products = productManagementService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Object> getProductsByCategory(@PathVariable String category) {
        try {
            Object products = productManagementService.getProductsByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-category")
    public ResponseEntity<Object> updateProductCategory(@RequestBody ProductCategoryUpdateDto updateDto) {
        try {
            Object result = productManagementService.updateProductCategory(updateDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId) {
        try {
            Object result = productManagementService.deleteProduct(productId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin Product Management Service is running");
    }
}