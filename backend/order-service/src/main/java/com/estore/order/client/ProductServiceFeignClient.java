package com.estore.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductServiceFeignClient {
    @GetMapping("/api/products/{id}")
    Object getProductById(@PathVariable("id") String id);

    @GetMapping("/api/products")
    Object getAllProducts();
}
