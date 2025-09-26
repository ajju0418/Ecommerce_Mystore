package com.estore.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {
    @GetMapping("/api/orders/user/{userId}")
    Object getOrdersByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/api/orders/{orderId}")
    Object getOrderById(@PathVariable("orderId") Long orderId);
}
