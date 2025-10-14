package com.estore.admin.controller;

import com.estore.admin.dto.ProductCategoryUpdateDto;
import com.estore.admin.service.ProductManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin Product Management")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "500", description = "Server error")
})
public class AdminProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);
    
    @Autowired
    private ProductManagementService productManagementService;

    @GetMapping("/all")
    @Operation(summary = "Get all products")
    public ResponseEntity<Object> getAllProducts() {
        logger.info("Getting all products");
        Object products = productManagementService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<Object> getProductsByCategory(
            @Parameter(example = "Electronics") @PathVariable String category) {
        logger.info("Getting products by category: {}", category != null ? category.replaceAll("[\r\n]", "") : "null");
        Object products = productManagementService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/update-category")
    @Operation(summary = "Update product category")
    @ApiResponse(responseCode = "400", description = "Invalid category")
    public ResponseEntity<Object> updateProductCategory(@RequestBody @Validated ProductCategoryUpdateDto updateDto) {
        logger.info("Updating product category for product: {}", updateDto.getProductId() != null ? updateDto.getProductId().replaceAll("[\r\n]", "") : "null");
        Object result = productManagementService.updateProductCategory(updateDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> deleteProduct(
            @Parameter(example = "123") @PathVariable @NotNull Long productId) {
        logger.info("Deleting product: {}", productId);
        Object result = productManagementService.deleteProduct(productId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create new product")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    public ResponseEntity<Object> createProduct(@RequestBody Object productDto) {
        logger.info("Creating new product");
        Object result = productManagementService.createProduct(productDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product")
    @ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid product data"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Object> updateProduct(
            @Parameter(example = "123") @PathVariable @NotNull Long productId, @RequestBody Object productDto) {
        logger.info("Updating product: {}", productId);
        Object result = productManagementService.updateProduct(productId, productDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin Product Management Service is running");
    }
}