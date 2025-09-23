import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, retry } from 'rxjs/operators';
import { ProductListItem } from '../models/product.types';
import { OrderService } from './order-service';
import { Productservice } from './productservice';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class cartService {
  private apiUrl = environment.apiUrl + '/cart';
  private cartItemsSubject = new BehaviorSubject<ProductListItem[]>([]);
  cartItems$: Observable<ProductListItem[]> = this.cartItemsSubject.asObservable();

  get cartCount(): number {
    return this.cartItemsSubject.value.reduce((count, item) => count + (item.quantity || 0), 0);
  }

  get cartCount$(): Observable<number> {
    return this.cartCountSubject.asObservable();
  }

  constructor(
    private http: HttpClient,
    private orderService: OrderService,
    private productService: Productservice
  ) {
    // Initialize cart count observable
    this.cartItemsSubject.subscribe(() => {
      this.cartCountSubject.next(this.cartCount);
    });
  }

  private cartCountSubject = new BehaviorSubject<number>(0);

  loadUserCart(userId: number): void {
    this.http.get<any>(`${this.apiUrl}/user/${userId}/cart`)
      .pipe(
        retry(2),
        catchError(this.handleError.bind(this))
      )
      .subscribe({
        next: (response) => {
          const items = response.items || [];
          if (items.length === 0) {
            this.cartItemsSubject.next([]);
            this.cartCountSubject.next(0);
            return;
          }
          // Enrich each cart item with product details
          let enrichedItems: ProductListItem[] = [];
          let completed = 0;
          items.forEach((item: any, idx: number) => {
            this.productService.getProductById(item.productId).subscribe({
              next: (product) => {
                enrichedItems[idx] = {
                  ...product,
                  quantity: item.quantity
                };
                completed++;
                if (completed === items.length) {
                  this.cartItemsSubject.next(enrichedItems);
                  this.cartCountSubject.next(this.cartCount);
                }
              },
              error: () => {
                // fallback if product not found
                enrichedItems[idx] = {
                  id: item.productId,
                  name: 'Unknown Product',
                  price: 0,
                  imageUrl: 'assets/default.jpg',
                  rating: 0,
                  quantity: item.quantity
                };
                completed++;
                if (completed === items.length) {
                  this.cartItemsSubject.next(enrichedItems);
                  this.cartCountSubject.next(this.cartCount);
                }
              }
            });
          });
        },
        error: (error) => console.error('Failed to load cart:', error)
      });
  }

  addToCart(product: ProductListItem, userId: number): Observable<any> {
    const cartItem = { productId: product.id, quantity: 1, userId };
    return new Observable(observer => {
      this.http.post(`${this.apiUrl}/add`, cartItem)
        .pipe(
          retry(1),
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (response) => {
            this.loadUserCart(userId);
            observer.next({ success: true, message: 'Product added to cart' });
            observer.complete();
          },
          error: (error) => {
            observer.next({ success: false, message: 'Failed to add to cart' });
            observer.complete();
          }
        });
    });
  }

  getCartItems(): ProductListItem[] {
    return this.cartItemsSubject.value;
  }

  updateQuantity(productId: string, quantity: number, userId: number): Observable<any> {
    return new Observable(observer => {
      this.http.put(`${this.apiUrl}/user/${userId}/product/${productId}?quantity=${quantity}`, {})
        .pipe(
          retry(1),
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (response) => {
            this.loadUserCart(userId);
            observer.next({ success: true, message: 'Quantity updated' });
            observer.complete();
          },
          error: (error) => {
            observer.next({ success: false, message: 'Failed to update quantity' });
            observer.complete();
          }
        });
    });
  }
  removeItem(productId: string, userId: number): Observable<any> {
    return new Observable(observer => {
      this.http.delete(`${this.apiUrl}/user/${userId}/product/${productId}`)
        .pipe(
          retry(1),
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (response) => {
            this.loadUserCart(userId);
            observer.next({ success: true, message: 'Item removed from cart' });
            observer.complete();
          },
          error: (error) => {
            observer.next({ success: false, message: 'Failed to remove item' });
            observer.complete();
          }
        });
    });
  }

  getTotalPrice(): number {
    return this.cartItemsSubject.value.reduce((total, item) => total + item.price * (item.quantity || 1), 0);
  }
  checkout(customerInfo: any, userId: number): Observable<any> {
    const items = this.cartItemsSubject.value;
    const totalAmount = this.getTotalPrice();
    if (items.length === 0) {
      throw new Error('Cart is empty');
    }
    return new Observable(observer => {
      this.orderService.createOrder(items, totalAmount, customerInfo).subscribe({
        next: (response) => {
          if (response.success) {
            this.clearCart(userId).subscribe({
              next: () => observer.next({ success: true, orderId: response.order.id }),
              error: (error) => observer.next({ success: false, message: 'Order created but failed to clear cart' })
            });
          } else {
            observer.next(response);
          }
          observer.complete();
        },
        error: (error) => {
          observer.next({ success: false, message: 'Failed to create order' });
          observer.complete();
        }
      });
    });
  }

  clearCart(userId: number): Observable<any> {
    return new Observable(observer => {
      this.http.delete(`${this.apiUrl}/user/${userId}/clear`)
        .pipe(
          retry(1),
          catchError(this.handleError.bind(this))
        )
        .subscribe({
          next: (response) => {
            this.cartItemsSubject.next([]);
            this.cartCountSubject.next(0);
            observer.next({ success: true, message: 'Cart cleared' });
            observer.complete();
          },
          error: (error) => {
            observer.next({ success: false, message: 'Failed to clear cart' });
            observer.complete();
          }
        });
    });
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error('Cart Service Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
