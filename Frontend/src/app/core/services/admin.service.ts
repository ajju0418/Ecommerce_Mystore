import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AdminLoginRequest {
  username: string;
  password: string;
}

export interface AdminResponse {
  success: boolean;
  message: string;
  token?: string;
  admin?: any;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = environment.apiUrl + '/admin';

  constructor(private http: HttpClient) {}

  login(loginData: AdminLoginRequest): Observable<any> {
    return new Observable(observer => {
      this.http.post(`${this.apiUrl}/auth/login`, loginData).subscribe({
        next: (response: any) => {
          if (response && response.id) {
            // Backend returns AdminResponseDto directly
            localStorage.setItem('currentAdmin', JSON.stringify(response));
            observer.next({ success: true, admin: response });
          } else {
            observer.next({ success: false, message: 'Invalid credentials' });
          }
          observer.complete();
        },
        error: (error) => {
          observer.next({ success: false, message: 'Login failed' });
          observer.complete();
        }
      });
    });
  }

  private getAuthHeaders(): HttpHeaders {
    const admin = JSON.parse(localStorage.getItem('currentAdmin') || '{}');
    const token = admin?.token;
    return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
  }

  getAllOrders(): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders/all`, { headers: this.getAuthHeaders() });
  }

  getOrderById(orderId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders/${orderId}`, { headers: this.getAuthHeaders() });
  }

  updateOrderStatus(orderId: string, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/orders/update-status`, { orderId: orderId, status: status }, { headers: this.getAuthHeaders() });
  }

  getAllProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/all`, { headers: this.getAuthHeaders() });
  }

  getProductsByCategory(category: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/category/${category}`, { headers: this.getAuthHeaders() });
  }

  updateProductCategory(productId: number, category: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/products/update-category`, { productId, category }, { headers: this.getAuthHeaders() });
  }
  
  acceptOrder(orderId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/orders/accept/${orderId}`, {}, { headers: this.getAuthHeaders() });
  }
  
  deleteOrder(orderId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/orders/${orderId}`, { headers: this.getAuthHeaders() });
  }
  
  markOrderAsDelivered(orderId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/orders/mark-delivered/${orderId}`, {}, { headers: this.getAuthHeaders() });
  }

  createProduct(product: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/products`, product, { headers: this.getAuthHeaders() });
  }

  updateProduct(id: number, product: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/products/${id}`, product, { headers: this.getAuthHeaders() });
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/products/${id}`, { headers: this.getAuthHeaders() });
  }

  getDashboardData(): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard`, { headers: this.getAuthHeaders() });
  }
}
