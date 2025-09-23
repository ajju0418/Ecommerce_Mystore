import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

  getAllOrders(): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders/all`);
  }

  getOrderById(orderId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders/${orderId}`);
  }

  updateOrderStatus(orderId: string, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/orders/update-status`, { 
      orderId: orderId, 
      status: status 
    });
  }

  getAllProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/all`);
  }

  getProductsByCategory(category: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/category/${category}`);
  }

  updateProductCategory(productId: number, category: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/products/update-category`, { productId, category });
  }
  
  acceptOrder(orderId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/orders/accept/${orderId}`, {});
  }
  
  removeOrder(orderId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/orders/remove/${orderId}`);
  }
  
  deleteOrder(orderId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/orders/${orderId}`);
  }
  
  deleteOrderDirect(orderId: string): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/orders/${orderId}`);
  }
  
  markOrderAsDelivered(orderId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/orders/mark-delivered/${orderId}`, {});
  }

  createProduct(product: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/products`, product);
  }

  updateProduct(id: number, product: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/products/${id}`, product);
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/products/${id}`);
  }

  getDashboardData(): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard`);
  }
}
