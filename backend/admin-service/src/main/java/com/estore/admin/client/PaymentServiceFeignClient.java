package com.estore.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "payment-service")
public interface PaymentServiceFeignClient {
    @GetMapping("/api/payments")
    Object getAllPayments();

    @GetMapping("/api/payments/{id}")
    Object getPaymentById(@PathVariable("id") Long id);

    @PostMapping("/api/payments")
    Object createPayment(@RequestBody Object paymentDto);

    @PutMapping("/api/payments/{id}")
    Object updatePayment(@PathVariable("id") Long id, @RequestBody Object paymentDto);

    @DeleteMapping("/api/payments/{id}")
    Object deletePayment(@PathVariable("id") Long id);
}
