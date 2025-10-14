package com.estore.product.controller;

import com.estore.product.entity.Product;
import com.estore.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "500", description = "Server error")
})
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/collection/{collection}")
    @Operation(summary = "Get products by collection")
    public List<Product> getProductsByCollection(
            @Parameter(example = "Summer2024") @PathVariable String collection) {
        return productService.getProductsByCollection(collection);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create product (Admin only)")
    @ApiResponse(responseCode = "403", description = "Admin role required")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "403", description = "Admin role required"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "403", description = "Admin role required"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products")
    public List<Product> searchProducts(@Parameter(example = "laptop") @RequestParam String q) {
        return productService.searchProducts(q);
    }

    @GetMapping("/gender/{gender}")
    @Operation(summary = "Get products by gender")
    public List<Product> getProductsByGender(@Parameter(example = "Male") @PathVariable String gender) {
        return productService.getProductsByGender(gender);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category")
    public List<Product> getProductsByCategory(@Parameter(example = "Electronics") @PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public String health() {
        return "Product Service is running";
    }
}