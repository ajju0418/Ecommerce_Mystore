package com.estore.payment.config;

import com.estore.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Component
@RestController
public class PaymentHealthIndicator {
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/actuator/payment-health")
    public Map<String, Object> getPaymentHealth() {
        Map<String, Object> health = new HashMap<>();
        try {
            long paymentCount = paymentRepository.count();
            Long completedPayments = paymentRepository.countCompletedPayments();
            Double totalRevenue = paymentRepository.getTotalCompletedPaymentAmount();
            health.put("status", "UP");
            health.put("service", "Payment Service");
            health.put("database", "Connected");
            health.put("totalPayments", paymentCount);
            health.put("completedPayments", completedPayments != null ? completedPayments : 0);
            health.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
            health.put("message", "All systems operational");
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("service", "Payment Service");
            health.put("database", "Connection failed");
            health.put("error", e.getMessage());
            health.put("message", "Service degraded");
        }
        return health;
    }
}