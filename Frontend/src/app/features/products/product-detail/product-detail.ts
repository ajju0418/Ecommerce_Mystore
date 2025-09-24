import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Header } from "../../../layout/header/header";
import { Footer } from "../../../layout/footer/footer";
import { Productservice } from '../../../core/services/productservice';
import { CartService } from '../../../core/services/cart-Service';
import { ProductListItem } from '../../../core/models/product.types';

interface ProductDetail {
  id: number;
  name: string;
  brand: string;
  price: number;
  rating: number;
  reviewCount: number;
  images: string[];
  colors: { name: string; value: string }[];
  sizes: string[];
  description: string;
  deliveryInfo: string;
}

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, Header, Footer],
  templateUrl: './product-detail.html',
  styleUrls: ['./product-detail.css']
})
export class ProductDetailComponent implements OnInit {
  product: ProductDetail | null = null;
  loading = false;
  selectedImageIndex = 0;
  selectedColor = '';
  selectedSize = '';
  quantity = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: Productservice,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.params['id'];
    this.loadProduct(productId);
  }

  loadProduct(id: string): void {
    this.loading = true;
    this.productService.getProductById(id).subscribe({
      next: (product: any) => {
        // Map backend product to ProductDetail if needed
        this.product = {
          id: product.id,
          name: product.name,
          brand: product.brand,
          price: product.price,
          rating: product.rating,
          reviewCount: product.reviewCount,
          images: product.images || [],
          colors: product.colors || [],
          sizes: product.sizes || [],
          description: product.description || '',
          deliveryInfo: product.deliveryInfo || ''
        };
        this.selectedColor = this.product.colors[0]?.name || '';
        this.selectedSize = this.product.sizes[0] || '';
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Failed to load product:', error);
        this.product = null;
        this.loading = false;
      }
    });
  }

  selectImage(index: number): void {
    this.selectedImageIndex = index;
  }

  selectColor(color: string): void {
    this.selectedColor = color;
  }

  selectSize(size: string): void {
    this.selectedSize = size;
  }

  addToCart(): void {
    if (!this.selectedSize) {
      alert('Please select a size');
      return;
    }
    // Integrate with CartService
    const currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    const userId = currentUser?.id;
    if (!userId) {
      alert('User not logged in');
      return;
    }
                const cartItem: ProductListItem = {
          id: this.product!.id.toString(),
          name: this.product!.name,
          price: this.product!.price,
          imageUrl: this.product!.images[0],
          rating: this.product!.rating,
          color: this.selectedColor,
          size: this.selectedSize,
          quantity: this.quantity
        };
        this.cartService.addToCart(cartItem, userId);
    console.log('Added to cart:', {
      productId: this.product!.id,
      color: this.selectedColor,
      size: this.selectedSize,
      quantity: this.quantity
    });
    alert('Product added to cart!');
  }

  getStars(rating: number): string[] {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(i <= rating ? 'filled' : 'empty');
    }
    return stars;
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }
}