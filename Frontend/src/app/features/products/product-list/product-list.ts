import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Header } from '../../../layout/header/header';
import { Footer } from '../../../layout/footer/footer';
import { Productservice } from '../../../core/services/productservice';
import { CartService } from '../../../core/services/cart-Service';
import { ProductListItem } from '../../../core/models/product.types';

interface Product {
  id: string;
  name: string;
  category: string;
  brand: string;
  price: number;
  originalPrice?: number;
  imageUrl: string;
  rating: number;
  reviewCount?: number;
  collection?: string;
  gender?: string;
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

  selectedDiscounts: number[] = [];
  selectedCollection = '';
  minPrice = 0;
  maxPrice = 1000;
  brandSearchTerm = '';
  
  // Sorting
  sortBy = 'popular';
  
  // Filter options (collections fixed to original set)
  categories: string[] = [];
  collections: string[] = ['New Arrivals', 'Premium', 'Casual', 'Sports'];
  discounts = [
    { value: 50, label: '50% or more' },
    { value: 40, label: '40% or more' },
    { value: 30, label: '30% or more' },
    { value: 20, label: '20% or more' },
    { value: 10, label: '10% or more' }
  ];
  
  // Mobile sidebar
  sidebarOpen = false;

  constructor(private router: Router, private route: ActivatedRoute, private productService: Productservice, private cartService: CartService) {}

  addToCart(product: Product, event: Event): void {
    event.stopPropagation();

    const currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    const userId = currentUser?.id;
    if (!userId) {
      alert('Please login to add items to cart');
      return;
    }

    const item: ProductListItem = {
      id: String(product.id),
      name: product.name,
      price: product.price,
      imageUrl: product.imageUrl,
      rating: product.rating,
      quantity: 1
    };

    this.cartService.addToCart(item, userId).subscribe(res => {
      if (res.success) {
        alert('Product added to cart!');
      } else {
        alert('Failed to add to cart');
      }
    });
  }

  ngOnInit(): void {
    // Check route data for deals
    const routeDeals = this.route.snapshot.data['deals'];
    
    this.route.queryParams.subscribe(params => {
      const gender = (params['gender'] || '').toString();
      const category = (params['category'] || '').toString();
      const collection = (params['collection'] || '').toString();
      const deals = (params['deals'] || '').toString();

      // Reset filters first
      this.selectedCategories = [];
      this.selectedDiscounts = [];
      this.selectedCollection = '';

      if (gender) {
        (this as any)._routeGender = gender;
      } else {
        (this as any)._routeGender = '';
      }
      if (category) {
        this.selectedCategories = [category];
      }
      if (deals === 'true' || routeDeals) {
        (this as any)._showDeals = true;
        this.loadProducts();
      } else {
        (this as any)._showDeals = false;
        if (collection) {
          this.selectedCollection = collection;
          this.loadProductsByCollection(collection);
        } else {
          this.loadProducts();
        }
      }
    });
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
          originalPrice: (p as any).originalPrice ?? 0,
          imageUrl: p.imageUrl ?? '',
          rating: p.rating ?? 0,
          reviewCount: 0,
          collection: p.collection ?? '',
          gender: (p as any).gender ?? ''
        }));
        // Populate filter options from loaded products to reflect database
        const categorySet = new Set<string>();
        this.products.forEach(p => {
          if (p.category) categorySet.add(p.category);
        });
        this.categories = Array.from(categorySet).sort();
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

  loadProductsByCollection(collection: string): void {
    this.loading = true;
    this.productService.getProductsByCollection(collection).subscribe({
      next: (products) => {
        this.products = products.map(p => ({
          id: p.id ?? '',
          name: p.name ?? '',
          category: p.category ?? '',
          brand: p.brand ?? '',
          price: p.price ?? 0,
          originalPrice: (p as any).originalPrice ?? 0,
          imageUrl: p.imageUrl ?? '',
          rating: p.rating ?? 0,
          reviewCount: 0,
          collection: p.collection ?? '',
          gender: (p as any).gender ?? ''
        }));
        this.filteredProducts = [...this.products]; // Show all products from collection
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load products by collection:', error);
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
    const routeGender = ((this as any)._routeGender || '').toLowerCase();
    const showDeals = (this as any)._showDeals || false;
    
    this.filteredProducts = this.products.filter(product => {
      const prodCategory = (product.category || '').toLowerCase();
      const prodBrand = (product.brand || '').toLowerCase();
      const prodCollection = ((product as any).collection || '').toLowerCase();
      const prodGender = ((product as any).gender || '').toLowerCase();

      // Deals filter - show only products with 30% or more discount
      if (showDeals) {
        const discountPercent = this.getDiscountPercentage(product);
        if (discountPercent < 30) {
          return false;
        }
      }

      const categoryMatch = this.selectedCategories.length === 0 || 
        this.selectedCategories.some(c => prodCategory === c.toLowerCase());

      const priceMatch = product.price >= this.minPrice && product.price <= this.maxPrice;

      const collectionMatch = !this.selectedCollection || 
        prodCollection === this.selectedCollection.toLowerCase();

      const genderMatch = !routeGender || prodGender === routeGender;

      return categoryMatch && priceMatch && collectionMatch && genderMatch;
    });
    
    // Sort deals by highest discount first
    if (showDeals) {
      this.filteredProducts.sort((a, b) => this.getDiscountPercentage(b) - this.getDiscountPercentage(a));
    } else {
      this.applySorting();
    }
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
    
    // Update URL with query params
    const queryParams: any = {};
    if (this.selectedCategories.length === 1) {
      queryParams.category = this.selectedCategories[0];
    }
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: 'merge'
    });
  }



  onDiscountChange(discount: number, checked: boolean): void {
    if (checked) {
      this.selectedDiscounts.push(discount);
    } else {
      this.selectedDiscounts = this.selectedDiscounts.filter(d => d !== discount);
    }
    this.applyFilters();
  }



  clearAllFilters(): void {
    this.selectedCategories = [];
    this.selectedDiscounts = [];
    this.selectedCollection = '';
    this.minPrice = 0;
    this.maxPrice = 1000;
    this.brandSearchTerm = '';
    this.applyFilters();
  }

  onCollectionChange(collection: string): void {
    // Reset all filters when "All Products" is selected
    if (collection === '') {
      this.selectedCategories = [];
      this.selectedDiscounts = [];
      (this as any)._routeGender = '';
      this.minPrice = 0;
      this.maxPrice = 1000;
      this.selectedCollection = '';
      this.loadProducts(); // Reload all products
    } else {
      this.selectedCollection = collection === this.selectedCollection ? '' : collection;
      if (this.selectedCollection) {
        this.loadProductsByCollection(this.selectedCollection);
      } else {
        this.loadProducts();
      }
    }
    
    // Update URL with query params
    const queryParams: any = {};
    if (this.selectedCollection) {
      queryParams.collection = this.selectedCollection;
    }
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: 'merge'
    });
  }

  onPriceChange(): void {
    this.applyFilters();
  }

  onSortChange(): void {
    this.applySorting();
  }

  onProductClick(productId: string): void {
    // No navigation; product details page removed
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

  getDiscountPercentage(product: any): number {
    if (product.originalPrice && product.price && product.originalPrice > product.price) {
      return Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100);
    }
    return 0;
  }
}