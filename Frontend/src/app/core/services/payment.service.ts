import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, delay } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface PaymentRequest {
  orderId: string;
  userId: number;
  amount: number;
  paymentMethod: string;
  cardNumber?: string;
  expiryDate?: string;
  cvv?: string;
  cardHolderName?: string;
  upiId?: string;
  bankName?: string;
  emiTenure?: string;
}

export interface PaymentResponse {
  success: boolean;
  message: string;
  transactionId?: string;
  status?: string;
  amount?: number;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = environment.apiUrl + '/payments';

  constructor(private http: HttpClient) {}

  processPayment(paymentRequest: PaymentRequest): Observable<PaymentResponse> {
    console.log('Payment service: Making API call to:', `${this.apiUrl}/process`);
    return this.http.post<PaymentResponse>(`${this.apiUrl}/process`, paymentRequest)
      .pipe(
        catchError(error => {
          console.error('Backend API error:', error);
          // If backend is not available, use mock payment for testing
          if (error.status === 0 || error.status === 404) {
            console.log('Backend not available, using mock payment');
            return this.mockPaymentProcess(paymentRequest);
          }
          return throwError(() => error);
        })
      );
  }

  private mockPaymentProcess(paymentRequest: PaymentRequest): Observable<PaymentResponse> {
    // Simulate payment processing delay
    return of({
      success: true,
      message: 'Payment processed successfully (Mock)',
      transactionId: 'MOCK-TXN-' + Date.now(),
      status: 'SUCCESS',
      amount: paymentRequest.amount
    }).pipe(delay(2000)); // 2 second delay to simulate processing
  }

  getPaymentStatus(transactionId: string): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(`${this.apiUrl}/transaction/${transactionId}`);
  }

  getPaymentByOrderId(orderId: string): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(`${this.apiUrl}/order/${orderId}`);
  }

  verifyPayment(transactionId: string): Observable<{verified: boolean}> {
    return this.http.get<{verified: boolean}>(`${this.apiUrl}/verify/${transactionId}`);
  }
}