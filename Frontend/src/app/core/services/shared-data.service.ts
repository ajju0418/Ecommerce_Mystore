import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ProductListItem } from '../models/product.types';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  private productsSubject = new BehaviorSubject<ProductListItem[]>([]);
  private ordersSubject = new BehaviorSubject<any[]>([]);

  products$ = this.productsSubject.asObservable();
  orders$ = this.ordersSubject.asObservable();

  constructor() {
    // No default products - data comes from backend
  }

  // Product methods
  getProducts(): ProductListItem[] {
    return this.productsSubject.value;
  }

  updateProducts(products: ProductListItem[]) {
    this.productsSubject.next(products);
  }

  addProduct(product: ProductListItem) {
    const currentProducts = this.productsSubject.value;
    const newProduct = { ...product, id: Date.now().toString() };
    const updatedProducts = [...currentProducts, newProduct];
    this.updateProducts(updatedProducts);
  }

  // Order methods
  getOrders(): any[] {
    return this.ordersSubject.value;
  }

  addOrder(order: any) {
    const currentOrders = this.ordersSubject.value;
    const updatedOrders = [...currentOrders, order];
    this.ordersSubject.next(updatedOrders);
  }

  updateOrdersFromService(orders: any[]) {
    this.ordersSubject.next(orders);
  }
}