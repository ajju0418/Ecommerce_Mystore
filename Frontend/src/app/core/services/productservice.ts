import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, retry } from 'rxjs/operators';
import { ProductListItem } from '../models/product.types';
import { environment } from '../../../environments/environment';

export interface Product {
  id?: string;
  name: string;
  description: string;
  price: number;
  originalPrice?: number;
  brand?: string;
  category: string;
  stock?: number;
  stockQuantity?: number; // Keep for backward compatibility
  collection: string;
  gender: string;
  imageUrl: string;
  rating: number;
  status?: string;
}

@Injectable({
  providedIn: 'root'
})
export class Productservice {
  private apiUrl = environment.apiUrl + '/products';

  constructor(private http: HttpClient) {}

  getProductList(): Observable<ProductListItem[]> {
    return this.http.get<ProductListItem[]>(this.apiUrl)
      .pipe(
        retry(2),
        catchError(this.handleError)
      );
  }

  getProductsByCategory(category: string): Observable<ProductListItem[]> {
    return this.http.get<ProductListItem[]>(`${this.apiUrl}/collection/${category}`)
      .pipe(
        retry(2),
        catchError(this.handleError)
      );
  }

  getProductById(id: string): Observable<ProductListItem> {
    return this.http.get<ProductListItem>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  searchProducts(searchTerm: string): Observable<ProductListItem[]> {
    return this.http.get<ProductListItem[]>(`${this.apiUrl}/search?q=${encodeURIComponent(searchTerm)}`)
      .pipe(
        retry(2),
        catchError(this.handleError)
      );
  }

  getProductsByGender(gender: string): Observable<ProductListItem[]> {
    return this.http.get<ProductListItem[]>(`${this.apiUrl}/gender/${gender}`)
      .pipe(
        retry(2),
        catchError(this.handleError)
      );
  }

  getProductsByCollection(collection: string): Observable<ProductListItem[]> {
    return this.http.get<ProductListItem[]>(`${this.apiUrl}/collection/${collection}`)
      .pipe(
        retry(2),
        catchError(this.handleError)
      );
  }

  addProduct(product: any) {
    return this.http.post(`${this.apiUrl}`, product)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl)
      .pipe(
        retry(2),
        catchError(this.handleError)
      );
  }

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  updateProduct(id: string, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  deleteProduct(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error('Product Service Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
