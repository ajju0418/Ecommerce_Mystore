package com.estore.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {
    @GetMapping("/api/orders/all")
    Object getAllOrders();

    @GetMapping("/api/orders/{id}")
    Object getOrderById(@PathVariable("id") Long id);

    @GetMapping("/api/orders/user/{userId}")
    Object getUserOrders(@PathVariable("userId") Long userId);

    @PutMapping("/api/orders/{id}/status")
    Object updateOrderStatus(@PathVariable("id") String id, @RequestBody Object statusUpdate);

    @DeleteMapping("/api/orders/{id}")
    Object deleteOrder(@PathVariable("id") Long id);
}
