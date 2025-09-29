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
            if (!isValidPaymentMethod(request.getPaymentMethod())) {
                return false;
            }

            // Accept UPI and Net Banking without card checks
            if (request.getPaymentMethod().equalsIgnoreCase("UPI") ||
                request.getPaymentMethod().equalsIgnoreCase("NET_BANKING") ||
                request.getPaymentMethod().equalsIgnoreCase("WALLET")) {
                return random.nextDouble() < 0.98;
            }

            // Handle EMI: require tenure and treat like card with slightly lower probability
            if (request.getPaymentMethod().equalsIgnoreCase("EMI")) {
                if (request.getEmiTenure() == null || request.getEmiTenure().isEmpty()) {
                    return false;
                }
                // If card details are present, validate them; else allow as bank EMI
                if (hasCardDetails(request) && !isValidCardDetails(request)) {
                    return false;
                }
                return random.nextDouble() < 0.95;
            }

            // Default card flow (CREDIT_CARD/DEBIT_CARD)
            if (hasCardDetails(request) && isValidCardDetails(request)) {
                return random.nextDouble() < 0.97;
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
                paymentMethod.equalsIgnoreCase("WALLET") ||
                paymentMethod.equalsIgnoreCase("EMI"));
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

    private boolean hasCardDetails(PaymentRequest request) {
        return request.getCardNumber() != null || request.getCvv() != null || request.getExpiryDate() != null;
    }
    
    public String generateGatewayResponse(boolean success) {
        if (success) {
            return "Payment processed successfully through gateway";
        } else {
            return "Payment declined by gateway";
        }
    }
}