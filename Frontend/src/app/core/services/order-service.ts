import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, retry, map, switchMap } from 'rxjs/operators';
import { of, forkJoin } from 'rxjs';
import { ProductListItem } from '../models/product.types';
import { Productservice } from './productservice';
import { environment } from '../../../environments/environment';

export interface PaymentDetails {
  method: string;
  transactionId: string;
  status: 'pending' | 'processing' | 'completed' | 'failed';
  timestamp: Date;
}

export interface Order {
  id: string;
  orderId?: string;
  userId?: number;
  userName?: string;
  userEmail?: string;
  userPhone?: string;
  items: ProductListItem[];
  orderItems?: any[];
  totalAmount: number;
  customerInfo: {
    name: string;
    email: string;
    phone: string;
    contact: number;
    address: string;
  };
  orderDate: Date;
  createdAt?: string;
  status: 'pending' | 'processing' | 'completed' | 'cancelled' | 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'CANCELLED' | 'paid' | 'PAID' | 'confirmed' | 'CONFIRMED' | 'shipped' | 'SHIPPED' | 'delivered' | 'DELIVERED' | 'failed' | 'FAILED';
  paymentDetails?: PaymentDetails;
}

export interface PreCheckoutOrder {
  items: ProductListItem[];
  totalAmount: number;
}

export interface PaymentOrder {
  items: ProductListItem[];
  totalAmount: number;
  customerDetails: any;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = environment.apiUrl + '/orders';
  private ordersSubject = new BehaviorSubject<Order[]>([]);
  public orders$ = this.ordersSubject.asObservable();

  private currentOrderSubject = new BehaviorSubject<Order | null>(null);
  public currentOrder$ = this.currentOrderSubject.asObservable();

  private preCheckoutOrderSubject = new BehaviorSubject<PreCheckoutOrder | null>(null);
  public preCheckoutOrder$ = this.preCheckoutOrderSubject.asObservable();

  private paymentOrderSubject = new BehaviorSubject<PaymentOrder | null>(null);
  public paymentOrder$ = this.paymentOrderSubject.asObservable();

  constructor(private http: HttpClient, private productService: Productservice) {}

  loadUserOrders(userId: number): void {
    this.http.get<any[]>(`${this.apiUrl}/user/${userId}`)
      .pipe(
        catchError(this.handleError.bind(this))
      )
      .subscribe({
        next: (orders) => {
          // Map backend orderItems to frontend items
          const mappedOrders = (orders || []).map(order => ({
            ...order,
            id: order.orderId,
            items: (order.orderItems || []).map((item: any) => ({
              id: item.productId,
              name: item.productName,
              price: item.price,
              quantity: item.quantity,
              imageUrl: item.imageUrl || 'assets/default.jpg',
              rating: item.rating || 0
            }))
          }));
          this.ordersSubject.next(mappedOrders);
        },
        error: (error) => console.error('Failed to load orders:', error)
      });
  }

  createOrder(items: ProductListItem[], totalAmount: number, customerInfo: any): Observable<any> {
    // Transform frontend data to match backend CheckoutDto structure
    const checkoutItems = items.map(item => ({
      productId: item.id,
      productName: item.name,
      price: item.price,
      quantity: item.quantity || 1
    }));

    const orderData = {
      userId: customerInfo.userId || customerInfo.id,
      items: checkoutItems,
      customerInfo: {
        name: customerInfo.name,
        email: customerInfo.email,
        phone: customerInfo.phone,
        address: customerInfo.address
      },
      totalAmount: totalAmount
    };

    return new Observable(observer => {
      this.http.post(`${this.apiUrl}/create`, orderData)
        .pipe(
          retry(1),
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (response: any) => {
            // Backend returns OrderDto directly
            this.currentOrderSubject.next(response);
            if (orderData.userId) {
              this.loadUserOrders(orderData.userId);
            }
            observer.next({ success: true, order: response });
            observer.complete();
          },
          error: (error) => {
            console.error('Order creation error:', error);
            observer.next({ success: false, message: 'Failed to create order' });
            observer.complete();
          }
        });
    });
  }

  getOrders(): Observable<Order[]> {
    return this.orders$;
  }

  // Admin method to get all orders from all users
  getAllOrders(): Observable<Order[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`)
      .pipe(
        map((orders: any[]) => {
          // Map backend data to frontend format
          return (orders || []).map(order => ({
            ...order,
            id: order.orderId || order.id,
            status: (order.status || 'pending').toString().toLowerCase() as Order['status'],
            userName: order.userName || order.customerInfo?.name || '',
            userEmail: order.userEmail || order.customerInfo?.email || '',
            userPhone: order.userPhone || order.customerInfo?.phone || '',
            orderDate: order.orderDate || order.createdAt || '',
            items: (order.orderItems || []).map((item: any) => ({
              id: item.productId,
              name: item.productName,
              price: item.price,
              quantity: item.quantity,
              imageUrl: item.imageUrl || 'assets/default.jpg',
              rating: item.rating || 0
            }))
          }));
        }),
        catchError(this.handleError.bind(this))
      );
  }

  getOrderById(id: string): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/${id}`);
  }

  updateOrderStatus(id: string, status: Order['status']): Observable<any> {
    return new Observable(observer => {
      const url = `${this.apiUrl}/${id}/status`;
      const body = { status: status.toString().toUpperCase() };
      
      console.log('=== FRONTEND ORDER STATUS UPDATE DEBUG ===');
      console.log('OrderService: Updating order status');
      console.log('OrderService: URL:', url);
      console.log('OrderService: Order ID:', id);
      console.log('OrderService: Status:', status);
      console.log('OrderService: Request body:', body);
      
      this.http.put(url, body)
        .pipe(
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (response: any) => {
            console.log('OrderService: Response received:', response);
            // Backend now returns a success wrapper with order data
            if (response && response.success) {
              const orders = this.ordersSubject.value.map(order =>
                order.id === id ? { ...order, status: status.toLowerCase() as Order['status'] } : order
              );
              this.ordersSubject.next(orders);
              observer.next({ success: true, message: response.message || 'Order status updated', order: response.order });
            } else {
              observer.next({ success: false, message: response?.message || 'Failed to update order status' });
            }
            observer.complete();
          },
          error: (error) => {
            console.error('OrderService: Update order status error:', error);
            console.error('OrderService: Error details:', {
              status: error.status,
              statusText: error.statusText,
              url: error.url,
              error: error.error,
              message: error.message
            });
            const errorMessage = error?.error?.message || error?.message || 'Failed to update order status';
            observer.next({ success: false, message: errorMessage });
            observer.complete();
          }
        });
    });
  }

  private generateOrderId(): string {
    return 'ORD-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
  }

  clearCurrentOrder(): void {
    this.currentOrderSubject.next(null);
  }

  setPreCheckoutOrder(order: PreCheckoutOrder): void {
    this.preCheckoutOrderSubject.next(order);
  }


  getAndClearPreCheckoutOrder(): PreCheckoutOrder | null {
    const order = this.preCheckoutOrderSubject.value;
    this.preCheckoutOrderSubject.next(null);
    return order;
  }

  setPaymentOrder(order: PaymentOrder): void {
    this.paymentOrderSubject.next(order);
  }

  getPaymentOrder(): PaymentOrder | null {
    return this.paymentOrderSubject.value;
  }

  clearPaymentOrder(): void {
    this.paymentOrderSubject.next(null);
  }

  createOrderWithPayment(items: ProductListItem[], totalAmount: number, customerInfo: any, paymentDetails: PaymentDetails): Observable<any> {
    // First create the order, then process payment
    return this.createOrder(items, totalAmount, customerInfo).pipe(
      switchMap((orderResponse) => {
        if (!orderResponse.success) {
          return of({ success: false, message: 'Failed to create order' });
        }
        
        // Now update the order with payment details
        return this.http.put(`${this.apiUrl}/${orderResponse.order.orderId || orderResponse.order.id}/payment`, paymentDetails)
          .pipe(
            map(response => ({ success: true, order: response })),
            catchError(error => {
              console.error('Payment update error:', error);
              return of({ success: false, message: 'Failed to update payment details' });
            })
          );
      })
    );
  }

  getOrderDetails(orderId: string): Observable<any> {
    return new Observable(observer => {
      this.http.get<any>(`${this.apiUrl}/orderdetails/${orderId}`)
        .pipe(
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (order) => {
            if (order) {
              // Map backend orderItems to frontend items
              const mappedOrder = {
                ...order,
                items: (order.orderItems || []).map((item: any) => ({
                  id: item.productId,
                  name: item.productName,
                  price: item.price,
                  quantity: item.quantity,
                  imageUrl: item.imageUrl || 'assets/default.jpg',
                  rating: item.rating || 0
                }))
              };
              observer.next({ success: true, order: mappedOrder });
            } else {
              observer.next({ success: false, message: 'Order not found' });
            }
            observer.complete();
          },
          error: (error) => {
            observer.next({ success: false, message: 'Failed to fetch order details' });
            observer.complete();
          }
        });
    });
  }

  cancelOrder(orderId: string): Observable<any> {
    return new Observable(observer => {
      this.http.put(`${this.apiUrl}/${orderId}/cancel`, {})
        .pipe(
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (response) => {
            const orders = this.ordersSubject.value.map(order =>
              order.id === orderId ? { ...order, status: 'cancelled' as const } : order
            );
            this.ordersSubject.next(orders);
            observer.next({ success: true, message: 'Order cancelled successfully' });
            observer.complete();
          },
          error: (error) => {
            observer.next({ success: false, message: 'Failed to cancel order' });
            observer.complete();
          }
        });
    });
  }


  fetchOrderById(orderId: string): Observable<Order> {
    return this.http.get<any>(`${this.apiUrl}/${orderId}`)
      .pipe(
        catchError(this.handleError.bind(this)),
        // Get user details and map to frontend Order
        switchMap(order => {
          console.log('Backend order response:', order);
          // Get user details from user service
          return of(null) // Skip user service call to avoid 403 error
            .pipe(
              switchMap(user => {
                const orderItems = order.orderItems || [];
                if (orderItems.length === 0) {
                  return of({
                    ...order,
                    items: [],
                    customerInfo: {
                      name: order.customerName || `Customer ${order.userId}`,
                      email: order.customerEmail || 'customer@example.com',
                      phone: order.customerPhone || '1234567890',
                      contact: order.customerPhone || `User ID: ${order.userId}`,
                      address: order.customerAddress || 'Not available'
                    },
                    orderDate: order.createdAt || order.orderDate || new Date(),
                  });
                }
                
                const productRequests = orderItems.map((item: any) => 
                  this.productService.getProductById(item.productId).pipe(
                    catchError(() => of({
                      id: item.productId,
                      name: item.productName,
                      price: item.price,
                      imageUrl: 'assets/default.jpg',
                      rating: 0
                    }))
                  )
                );
                
                return forkJoin(productRequests).pipe(
                  map(products => ({
                    ...order,
                    items: (products as any[]).map((product: any, index: number) => ({
                      ...product,
                      quantity: orderItems[index].quantity
                    })),
                    customerInfo: {
                      name: order.customerName || `Customer ${order.userId}`,
                      email: order.customerEmail || 'customer@example.com',
                      phone: order.customerPhone || '1234567890',
                      contact: order.customerPhone || `User ID: ${order.userId}`,
                      address: order.customerAddress || 'Not available'
                    },
                    orderDate: order.createdAt || order.orderDate || new Date(),
                  }))
                );
              })
            );
        })
      );
  }

  placeOrder(order: Order): Observable<any> {
    return this.createOrder(order.items, order.totalAmount, {
      ...order.customerInfo,
      userId: order.userId
    });
  }

  // Update deleteOrder to handle plain text response from backend
  deleteOrder(orderId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${orderId}`, { responseType: 'text' })
      .pipe(
        catchError(this.handleError.bind(this))
      );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error('Order Service Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
