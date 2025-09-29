import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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
    return this.http.post<PaymentResponse>(`${this.apiUrl}/process`, paymentRequest);
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