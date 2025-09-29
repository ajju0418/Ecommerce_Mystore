import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Header } from '../../../layout/header/header';
import { Footer } from '../../../layout/footer/footer';
import { Productservice } from '../../../core/services/productservice';
import { CartService } from '../../../core/services/cart-Service';

interface Product {
  id: string;
  name: string;
  category: string;
  brand: string;
  price: number;
  imageUrl: string;
  rating: number;
  reviewCount?: number;
  collection?: string;
}

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, Header, Footer],
  templateUrl: './product-list.html',
  styleUrls: ['./product-list.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  loading = false;
  
  // Filters
  selectedCategories: string[] = [];
  selectedBrands: string[] = [];
  selectedRatings: number[] = [];
  selectedDiscounts: number[] = [];
  selectedCollection = '';
  minPrice = 0;
  maxPrice = 1000;
  brandSearchTerm = '';
  
  // Sorting
  sortBy = 'popular';
  
  // Filter options
  categories = ['Shirts', 'T-Shirts', 'Jeans', 'Jackets', 'Accessories', 'Pants', 'Sweaters', 'Hoodies', 'Shoes'];
  brands = ['Nike', 'Adidas', 'Puma', 'H&M', 'Zara'];
  collections = ['New Arrivals', 'Premium', 'Casual', 'Sports', 'Men\'s', 'Women\'s', 'Kids', 'Accessories'];
  ratings = [
    { value: 4, text: '4★ & above', stars: [1,1,1,1], emptyStars: [1] },
    { value: 3, text: '3★ & above', stars: [1,1,1], emptyStars: [1,1] },
    { value: 2, text: '2★ & above', stars: [1,1], emptyStars: [1,1,1] },
    { value: 1, text: '1★ & above', stars: [1], emptyStars: [1,1,1,1] }
  ];
  discounts = [
    { value: 50, label: '50% or more' },
    { value: 40, label: '40% or more' },
    { value: 30, label: '30% or more' },
    { value: 20, label: '20% or more' },
    { value: 10, label: '10% or more' }
  ];
  
  // Mobile sidebar
  sidebarOpen = false;

  constructor(private router: Router, private productService: Productservice, private cartService: CartService) {}

  addToCart(product: Product, event: Event): void {
    event.stopPropagation();
    
    // For now, use localStorage until backend cart is properly configured
    const cartItems = JSON.parse(localStorage.getItem('cartItems') || '[]');
    const existingItem = cartItems.find((item: any) => item.id === product.id);
    
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      cartItems.push({
        id: product.id,
        name: product.name,
        price: product.price,
        imageUrl: product.imageUrl,
        quantity: 1
      });
    }
    
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
    alert('Product added to cart!');
  }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products = products.map(p => ({
          id: p.id ?? '',
          name: p.name ?? '',
          category: p.category ?? '',
          brand: p.brand ?? '',
          price: p.price ?? 0,
          imageUrl: p.imageUrl ?? '',
          rating: p.rating ?? 0,
          reviewCount: 0,
          collection: p.collection ?? ''
        }));
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load products:', error);
        this.products = [];
        this.filteredProducts = [];
        this.loading = false;
      }
    });
  }

  getMockProducts(): Product[] {
    return [
      {
        id: '1',
        name: 'Air Max 270',
        category: "Men's Running Shoes",
        brand: 'Nike',
        price: 150.00,
        imageUrl: 'assets/product-shirts/image1.jpg',
        rating: 4.5,
        reviewCount: 128
      },
      {
        id: '2',
        name: 'Ultraboost 22',
        category: "Men's Running Shoes",
        brand: 'Adidas',
        price: 180.00,
        imageUrl: 'assets/product-shirts/image2.jpg',
        rating: 4.7,
        reviewCount: 95
      },
      {
        id: '3',
        name: 'Classic Polo',
        category: "Men's Casual Wear",
        brand: 'Puma',
        price: 45.00,
        imageUrl: 'assets/product-shirts/image3.jpg',
        rating: 4.2,
        reviewCount: 67
      },
      {
        id: '4',
        name: 'Training Shorts',
        category: "Men's Athletic Wear",
        brand: 'Under Armour',
        price: 35.00,
        imageUrl: 'assets/product-shirts/image4.jpg',
        rating: 4.3,
        reviewCount: 42
      }
    ];
  }

  applyFilters(): void {
    this.filteredProducts = this.products.filter(product => {
      const categoryMatch = this.selectedCategories.length === 0 || 
                           this.selectedCategories.includes(product.category);
      const brandMatch = this.selectedBrands.length === 0 || 
                        this.selectedBrands.includes(product.brand);
      const priceMatch = product.price >= this.minPrice && product.price <= this.maxPrice;
      const ratingMatch = this.selectedRatings.length === 0 || 
                         this.selectedRatings.some(rating => product.rating >= rating);
      const collectionMatch = !this.selectedCollection || 
                             (product as any).collection === this.selectedCollection;
      
      return categoryMatch && brandMatch && priceMatch && ratingMatch && collectionMatch;
    });
    
    this.applySorting();
  }

  applySorting(): void {
    switch (this.sortBy) {
      case 'popular':
        this.filteredProducts.sort((a, b) => b.rating - a.rating);
        break;
      case 'newest':
        this.filteredProducts.sort((a, b) => parseInt(b.id) - parseInt(a.id));
        break;
      case 'price-low':
        this.filteredProducts.sort((a, b) => a.price - b.price);
        break;
      case 'price-high':
        this.filteredProducts.sort((a, b) => b.price - a.price);
        break;
    }
  }

  onCategoryChange(category: string, checked: boolean): void {
    if (checked) {
      this.selectedCategories.push(category);
    } else {
      this.selectedCategories = this.selectedCategories.filter(c => c !== category);
    }
    this.applyFilters();
  }

  onBrandChange(brand: string, checked: boolean): void {
    if (checked) {
      this.selectedBrands.push(brand);
    } else {
      this.selectedBrands = this.selectedBrands.filter(b => b !== brand);
    }
    this.applyFilters();
  }

  onRatingChange(rating: number, checked: boolean): void {
    if (checked) {
      this.selectedRatings.push(rating);
    } else {
      this.selectedRatings = this.selectedRatings.filter(r => r !== rating);
    }
    this.applyFilters();
  }

  onDiscountChange(discount: number, checked: boolean): void {
    if (checked) {
      this.selectedDiscounts.push(discount);
    } else {
      this.selectedDiscounts = this.selectedDiscounts.filter(d => d !== discount);
    }
    this.applyFilters();
  }

  getFilteredBrands(): string[] {
    return this.brands.filter(brand => 
      brand.toLowerCase().includes(this.brandSearchTerm.toLowerCase())
    );
  }

  clearAllFilters(): void {
    this.selectedCategories = [];
    this.selectedBrands = [];
    this.selectedRatings = [];
    this.selectedDiscounts = [];
    this.selectedCollection = '';
    this.minPrice = 0;
    this.maxPrice = 1000;
    this.brandSearchTerm = '';
    this.applyFilters();
  }

  onCollectionChange(collection: string): void {
    this.selectedCollection = collection === this.selectedCollection ? '' : collection;
    this.applyFilters();
  }

  onPriceChange(): void {
    this.applyFilters();
  }

  onSortChange(): void {
    this.applySorting();
  }

  onProductClick(productId: string): void {
    this.router.navigate(['/product', productId]);
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  getStars(rating: number): string[] {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(i <= rating ? 'filled' : 'empty');
    }
    return stars;
  }
}