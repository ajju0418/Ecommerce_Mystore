import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderService } from '../../core/services/order-service';
import { UserService } from '../../core/services/user-service';
import { PaymentService, PaymentRequest } from '../../core/services/payment.service';
import { ProductListItem } from '../../core/models/product.types';
import { Header } from '../../layout/header/header';
import { Footer } from '../../layout/footer/footer';
import { CartService } from '../../core/services/cart-Service';


@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule, Header, Footer ],
  templateUrl: './payment.html',
  styleUrls: ['./payment.css']
})
export class PaymentComponent implements OnInit {
  orderDetails: ProductListItem[] = [];
  totalAmount: number = 0;
  customerDetails: any = {};
  
  selectedPaymentMethod: string = 'card';
  
  // Card details
  cardNumber: string = '';
  expiryDate: string = '';
  cvv: string = '';
  cardHolderName: string = '';
  
  // UPI details
  upiId: string = '';
  
  // Net Banking
  selectedBank: string = '';
  

  
  // EMI
  selectedEmiTenure: string = '';
  
  isProcessing: boolean = false;
  paymentStep: number = 1;
  paymentStatus: 'pending' | 'processing' | 'success' | 'failed' = 'pending';
  transactionId: string = '';
  paymentFailureReason: string = '';

  constructor(
    private orderService: OrderService, 
    private cartService: CartService,
    private userService: UserService,
    private paymentService: PaymentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const orderData = this.orderService.getPaymentOrder();
    if (orderData) {
      this.orderDetails = orderData.items;
      this.totalAmount = orderData.totalAmount;
      this.customerDetails = orderData.customerDetails;
    } else {
      alert('No order found. Redirecting to cart.');
      this.router.navigate(['/home/cart']);
    }
  }

  selectPaymentMethod(method: string): void {
    this.selectedPaymentMethod = method;
  }

  processPayment(form: NgForm): void {
    console.log('processPayment called, form valid:', form.valid);
    if (form.invalid) {
      form.form.markAllAsTouched();
      return;
    }

    const currentUser = this.userService.getCurrentUser();
    if (!currentUser?.id) {
      alert('Please login to continue payment');
      return;
    }

    console.log('Starting payment processing...');
    this.isProcessing = true;
    this.paymentStatus = 'processing';
    this.paymentStep = 2;
    console.log('Payment step set to:', this.paymentStep);
    
    // Create order first to get orderId
    this.orderService.createOrder(this.orderDetails, this.totalAmount, this.customerDetails).subscribe({
      next: (orderResponse) => {
        console.log('Order created:', orderResponse);
        if (orderResponse.success && orderResponse.order) {
          // Process payment with backend
          const paymentRequest: PaymentRequest = {
            orderId: orderResponse.order.orderId || orderResponse.order.id,
            userId: currentUser.id!,
            amount: this.totalAmount,
            paymentMethod: this.selectedPaymentMethod,
            cardNumber: this.cardNumber,
            expiryDate: this.expiryDate,
            cvv: this.cvv,
            cardHolderName: this.cardHolderName,
            upiId: this.upiId
          };

          // Process payment with the backend payment service
          this.paymentService.processPayment(paymentRequest).subscribe({
            next: (paymentResponse) => {
              console.log('Payment processing complete:', paymentResponse);
              this.isProcessing = false;
              
              if (paymentResponse.success) {
                this.paymentStatus = 'success';
                this.paymentStep = 3;
                this.transactionId = paymentResponse.transactionId || this.generateTransactionId();
                console.log('Payment step set to:', this.paymentStep, 'Status:', this.paymentStatus);
                
                // Clear cart and redirect
                this.cartService.clearCart(currentUser.id!).subscribe();
                this.orderService.clearPaymentOrder();
                
                setTimeout(() => {
                  this.router.navigate(['/order-details', orderResponse.order.orderId]);
                }, 2000);
              } else {
                this.paymentStatus = 'failed';
                this.paymentFailureReason = paymentResponse.message || 'Payment processing failed';
                this.paymentStep = 4;
              }
            },
            error: (error) => {
              console.error('Payment processing error:', error);
              this.isProcessing = false;
              this.paymentStatus = 'failed';
              this.paymentFailureReason = 'Payment service error. Please try again.';
              this.paymentStep = 4;
            }
          });
        } else {
          this.isProcessing = false;
          this.paymentStatus = 'failed';
          this.paymentFailureReason = 'Failed to create order';
          this.paymentStep = 4;
        }
      },
      error: (error) => {
        this.isProcessing = false;
        this.paymentStatus = 'failed';
        this.paymentFailureReason = 'Failed to create order. Please try again.';
        this.paymentStep = 4;
      }
    });
  }

  private generateTransactionId(): string {
    return 'TXN' + Date.now() + Math.random().toString(36).substr(2, 6).toUpperCase();
  }

  private getRandomFailureReason(): string {
    const reasons = [
      'Insufficient funds in account',
      'Card expired or invalid',
      'Transaction declined by bank',
      'Network timeout occurred',
      'Invalid CVV entered'
    ];
    return reasons[Math.floor(Math.random() * reasons.length)];
  }

  retryPayment(): void {
    this.paymentStatus = 'pending';
    this.paymentStep = 1;
    this.transactionId = '';
    this.paymentFailureReason = '';
  }

  goBack(): void {
    if (this.paymentStep === 1) {
      this.router.navigate(['/order-summary']);
    } else {
      this.retryPayment();
    }
  }
}