package com.estore.product.controller;

import com.estore.product.entity.Product;
import com.estore.product.service.ProductFilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products/filter")
@Tag(name = "Product Filter")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "500", description = "Server error")
})
public class ProductFilterController {
    
    private final ProductFilterService productFilterService;
    
    public ProductFilterController(ProductFilterService productFilterService) {
        this.productFilterService = productFilterService;
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category")
    public List<Product> getProductsByCategory(
            @Parameter(example = "Electronics") @PathVariable String category) {
        return productFilterService.getProductsByCategory(category);
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get products by brand")
    public List<Product> getProductsByBrand(
            @Parameter(example = "Samsung") @PathVariable String brand) {
        return productFilterService.getProductsByBrand(brand);
    }

    @GetMapping("/brand/{brand}/category/{category}")
    @Operation(summary = "Get products by brand and category")
    public List<Product> getProductsByBrandAndCategory(
            @Parameter(example = "Samsung") @PathVariable String brand,
            @Parameter(example = "Electronics") @PathVariable String category) {
        return productFilterService.getProductsByBrandAndCategory(brand, category);
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Get out of stock products")
    public List<Product> getOutOfStockProducts() {
        return productFilterService.getOutOfStockProducts();
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get inactive products")
    public List<Product> getInactiveProducts() {
        return productFilterService.getInactiveProducts();
    }
}
