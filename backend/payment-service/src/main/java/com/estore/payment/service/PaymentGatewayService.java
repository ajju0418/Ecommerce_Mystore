package com.estore.payment.service;

import com.estore.payment.dto.PaymentRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentGatewayService {
    
    private final Random random = new Random();
    
    public boolean processPayment(PaymentRequest request) {
        try {
            // Simulate payment gateway processing time
            Thread.sleep(2000);
            
            // Simulate payment gateway logic
            if (isValidPaymentMethod(request.getPaymentMethod())) {
                if (isValidCardDetails(request)) {
                    // Simulate 95% success rate
                    return random.nextDouble() < 0.95;
                }
            }
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null && 
               (paymentMethod.equalsIgnoreCase("CREDIT_CARD") ||
                paymentMethod.equalsIgnoreCase("DEBIT_CARD") ||
                paymentMethod.equalsIgnoreCase("UPI") ||
                paymentMethod.equalsIgnoreCase("NET_BANKING") ||
                paymentMethod.equalsIgnoreCase("WALLET"));
    }
    
    private boolean isValidCardDetails(PaymentRequest request) {
        // Basic validation for card details
        if (request.getPaymentMethod().contains("CARD")) {
            return request.getCardNumber() != null && 
                   request.getCardNumber().length() >= 16 &&
                   request.getExpiryDate() != null &&
                   request.getCvv() != null &&
                   request.getCvv().length() >= 3;
        }
        return true; // For non-card payments
    }
    
    public String generateGatewayResponse(boolean success) {
        if (success) {
            return "Payment processed successfully through gateway";
        } else {
            return "Payment declined by gateway";
        }
    }
}