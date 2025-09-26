package com.estore.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service")
public interface ProductServiceFeignClient {
    @GetMapping("/api/products")
    Object getAllProducts();

    @GetMapping("/api/products/{id}")
    Object getProductById(@PathVariable("id") Long id);

    @PostMapping("/api/products")
    Object createProduct(@RequestBody Object productDto);

    @PutMapping("/api/products/{id}")
    Object updateProduct(@PathVariable("id") Long id, @RequestBody Object productDto);

    @DeleteMapping("/api/products/{id}")
    Object deleteProduct(@PathVariable("id") Long id);
}
