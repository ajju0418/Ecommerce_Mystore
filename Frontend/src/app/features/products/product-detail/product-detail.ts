import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Header } from "../../../layout/header/header";
import { Footer } from "../../../layout/footer/footer";

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
    private router: Router
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.params['id'];
    this.loadProduct(productId);
  }

  loadProduct(id: string): void {
    this.loading = true;
    
    // TODO: Replace with actual backend service call
    // this.productService.getProductById(+id).subscribe(product => {
    //   this.product = product;
    //   this.selectedColor = product.colors[0]?.name || '';
    //   this.selectedSize = product.sizes[0] || '';
    //   this.loading = false;
    // });
    
    // Mock data for now
    setTimeout(() => {
      this.product = this.getMockProduct(+id);
      this.selectedColor = this.product.colors[0]?.name || '';
      this.selectedSize = this.product.sizes[0] || '';
      this.loading = false;
    }, 500);
  }

  getMockProduct(id: number): ProductDetail {
    return {
      id,
      name: 'Air Max 270',
      brand: 'Nike',
      price: 150.00,
      rating: 4.5,
      reviewCount: 128,
      images: [
        'assets/product-shirts/image1.jpg',
        'assets/product-shirts/image2.jpg',
        'assets/product-shirts/image3.jpg',
        'assets/product-shirts/image4.jpg'
      ],
      colors: [
        { name: 'Black', value: '#000000' },
        { name: 'White', value: '#FFFFFF' },
        { name: 'Blue', value: '#0066CC' },
        { name: 'Red', value: '#CC0000' }
      ],
      sizes: ['7', '8', '9', '10', '11', '12'],
      description: 'The Nike Air Max 270 delivers visible Air cushioning under every step. Inspired by the Air Max 93 and Air Max 180, it features Nike\'s largest heel Air unit yet for a super-soft ride that feels as impossible as it looks.',
      deliveryInfo: 'Free delivery on orders over $30'
    };
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
    
    // TODO: Implement add to cart functionality
    // this.cartService.addToCart({
    //   productId: this.product!.id,
    //   color: this.selectedColor,
    //   size: this.selectedSize,
    //   quantity: this.quantity
    // });
    
    console.log('Added to cart:', {
      product: this.product,
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