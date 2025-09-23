package com.estore.product.controller;

import com.estore.product.entity.Product;
import com.estore.product.service.ProductFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products/filter")
public class ProductFilterController {
    @Autowired
    private ProductFilterService productFilterService;

    @GetMapping(value = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productFilterService.getProductsByCategory(category);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(products);
    }

    @GetMapping(value = "/brand/{brand}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> products = productFilterService.getProductsByBrand(brand);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(products);
    }

    @GetMapping(value = "/brand/{brand}/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getProductsByBrandAndCategory(@PathVariable String brand, @PathVariable String category) {
        List<Product> products = productFilterService.getProductsByBrandAndCategory(brand, category);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(products);
    }

    @GetMapping(value = "/test-json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object>> testJson() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Collections.emptyList());
    }
}
