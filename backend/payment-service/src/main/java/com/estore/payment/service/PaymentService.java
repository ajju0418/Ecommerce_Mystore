package com.estore.payment.service;

import com.estore.payment.dto.PaymentRequest;
import com.estore.payment.dto.PaymentResponse;
import com.estore.payment.entity.Payment;
import com.estore.payment.entity.PaymentStatus;
import com.estore.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${order.service.url:http://localhost:9092/api/orders}")
    private String orderServiceUrl;

    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            // Create payment record
            Payment payment = new Payment(
                request.getOrderId(),
                request.getUserId(),
                request.getPaymentMethod(),
                request.getAmount()
            );
            
            payment.setCustomerName(request.getCustomerName());
            payment.setCustomerEmail(request.getCustomerEmail());
            payment.setCustomerPhone(request.getCustomerPhone());
            payment.setBillingAddress(request.getBillingAddress());
            payment.setStatus(PaymentStatus.PROCESSING);
            
            // Save initial payment record
            payment = paymentRepository.save(payment);
            
            // Process payment through gateway
            boolean gatewayResult = paymentGatewayService.processPayment(request);

            if (gatewayResult) {
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setPaymentDate(LocalDateTime.now());
                payment.setGatewayResponse("Payment processed successfully");
                // Update order status to PAID
                updateOrderStatus(request.getOrderId(), "PAID");
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setGatewayResponse("Payment processing failed");
                // Update order status to FAILED
                updateOrderStatus(request.getOrderId(), "FAILED");
            }
            
            payment = paymentRepository.save(payment);
            
            return mapToPaymentResponse(payment);
            
        } catch (Exception e) {
            // Handle payment failure
            Payment failedPayment = new Payment(
                request.getOrderId(),
                request.getUserId(),
                request.getPaymentMethod(),
                request.getAmount()
            );
            failedPayment.setStatus(PaymentStatus.FAILED);
            failedPayment.setGatewayResponse("Payment processing error: " + e.getMessage());
            paymentRepository.save(failedPayment);
            
            updateOrderStatus(request.getOrderId(), "FAILED");

            return new PaymentResponse(
                request.getOrderId(),
                failedPayment.getTransactionId(),
                PaymentStatus.FAILED,
                "Payment processing failed: " + e.getMessage()
            );
        }
    }
    
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Optional<Payment> payment = paymentRepository.findByTransactionId(transactionId);
        if (payment.isPresent()) {
            return mapToPaymentResponse(payment.get());
        }
        throw new RuntimeException("Payment not found with transaction ID: " + transactionId);
    }
    
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        if (payment.isPresent()) {
            return mapToPaymentResponse(payment.get());
        }
        throw new RuntimeException("Payment not found for order ID: " + orderId);
    }
    
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public PaymentResponse updatePaymentStatus(String transactionId, PaymentStatus status) {
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(transactionId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(status);
            if (status == PaymentStatus.COMPLETED) {
                payment.setPaymentDate(LocalDateTime.now());
            }
            payment = paymentRepository.save(payment);
            return mapToPaymentResponse(payment);
        }
        throw new RuntimeException("Payment not found with transaction ID: " + transactionId);
    }
    
    public boolean verifyPayment(String transactionId) {
        Optional<Payment> payment = paymentRepository.findByTransactionId(transactionId);
        return payment.isPresent() && payment.get().getStatus() == PaymentStatus.COMPLETED;
    }
    
    public Long getTotalCompletedPayments() {
        return paymentRepository.countCompletedPayments();
    }
    
    public Double getTotalRevenue() {
        return paymentRepository.getTotalCompletedPaymentAmount();
    }
    
    private PaymentResponse mapToPaymentResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setTransactionId(payment.getTransactionId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentDate(payment.getPaymentDate());
        response.setSuccess(payment.getStatus() == PaymentStatus.COMPLETED);
        
        switch (payment.getStatus()) {
            case COMPLETED:
                response.setMessage("Payment completed successfully");
                break;
            case FAILED:
                response.setMessage("Payment failed");
                break;
            case PENDING:
                response.setMessage("Payment is pending");
                break;
            case PROCESSING:
                response.setMessage("Payment is being processed");
                break;
            default:
                response.setMessage("Payment status: " + payment.getStatus());
        }
        
        return response;
    }

    private void updateOrderStatus(String orderId, String status) {
        try {
            String url = orderServiceUrl + "/" + orderId + "/status";
            HashMap<String, String> body = new HashMap<>();
            body.put("status", status);
            restTemplate.put(url, body);
        } catch (Exception ex) {
            // Log error but do not fail payment processing
            System.err.println("Failed to update order status: " + ex.getMessage());
        }
    }
}