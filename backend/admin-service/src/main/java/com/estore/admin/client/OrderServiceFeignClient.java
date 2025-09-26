package com.estore.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {
    @GetMapping("/api/orders")
    Object getAllOrders();

    @GetMapping("/api/orders/{id}")
    Object getOrderById(@PathVariable("id") Long id);

    @PostMapping("/api/orders")
    Object createOrder(@RequestBody Object orderDto);

    @PutMapping("/api/orders/{id}")
    Object updateOrder(@PathVariable("id") Long id, @RequestBody Object orderDto);

    @DeleteMapping("/api/orders/{id}")
    Object deleteOrder(@PathVariable("id") Long id);
}
