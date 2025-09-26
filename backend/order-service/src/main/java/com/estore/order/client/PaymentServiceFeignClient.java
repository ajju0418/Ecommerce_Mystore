package com.estore.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentServiceFeignClient {
    @GetMapping("/api/payments/{id}")
    Object getPaymentById(@PathVariable("id") String id);

    @GetMapping("/api/payments")
    Object getAllPayments();

    @PostMapping("/api/payments")
    Object createPayment(@RequestBody Object paymentDto);
}
