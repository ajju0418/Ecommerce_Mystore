import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Productservice as AdminProductService, Product } from '../../../core/services/productservice';
import { ProductListItem } from '../../../core/models/product.types';


@Component({
  selector: 'app-admin-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-products.html',
  styleUrl: './admin-products.css'
})
export class AdminProducts implements OnInit {
  products: ProductListItem[] = [];
  filteredProducts: ProductListItem[] = [];
  searchTerm = '';
  selectedCategory = '';
  selectedStatus = '';
  showAddForm = false;
  showEditForm = false;
  editingProduct: ProductListItem | null = null;
  newProduct: any = {
    name: '',
    category: '',
    collection: '',
    gender: '',
    price: 0,
    originalPrice: 0,
    brand: '',
    stock: 0,
    description: '',
    imageUrl: '',
    rating: 4.0,
    status: 'ACTIVE'
  };
  categories = ['Shirts', 'T-Shirts', 'Jeans', 'Jackets', 'Shoes', 'Accessories', 'Pants', 'Sweaters', 'Hoodies'];
  collections = ['New Arrivals', 'Premium', 'Casual', 'Sports', 'Men\'s', 'Women\'s',  'Accessories'];

  constructor(

    private adminProductService: AdminProductService
  ) {}

  ngOnInit() {
    // Always load products from backend database
    this.loadProducts();
  }

  private mapProductToListItem(product: Product): ProductListItem {
    return {
      id: product.id ? String(product.id) : '',
      name: product.name,
      price: product.price,
      imageUrl: product.imageUrl ?? 'assets/images/no-image.png',
      rating: product.rating ?? 0,
      category: product.category,
      description: product.description,
      stock: (product as any).stock || 0,
      status: (product as any).status || 'ACTIVE',
      collection: product.collection,
      gender: product.gender,
      brand: (product as any).brand,
      originalPrice: (product as any).originalPrice
    };
  }

  loadProducts() {
    // Fetch products from backend database
    this.adminProductService.getProducts().subscribe({
      next: (products: Product[]) => {
        this.products = products.map(this.mapProductToListItem);
        this.applyFilters();
      },
      error: (error: any) => {
        console.error('Failed to load products:', error);
        this.products = [];
      }
    });
  }

  applyFilters() {
    if (this.selectedCategory) {
      this.adminProductService.getProductsByCategory(this.selectedCategory).subscribe({
        next: (products: ProductListItem[]) => {
          this.filteredProducts = products.filter((product: ProductListItem) => {
            const matchesSearch =
              !this.searchTerm ||
              product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||

              (product.collection && product.collection.toLowerCase().includes(this.searchTerm.toLowerCase()));
            const matchesStatus = !this.selectedStatus || product.status === this.selectedStatus;
            return matchesSearch && matchesStatus;
          });
        },
        error: () => {
          this.filteredProducts = [];
        }
      });
    } else {
      this.filteredProducts = this.products.filter((product: ProductListItem) => {
        const matchesSearch =
          !this.searchTerm ||
          product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          (product.collection && product.collection.toLowerCase().includes(this.searchTerm.toLowerCase()));
        const matchesStatus = !this.selectedStatus || product.status === this.selectedStatus;
        return matchesSearch && matchesStatus;
      });
    }
  }

  addProduct() {
    // Add product to backend database
    if (this.newProduct.name && this.newProduct.collection && this.newProduct.price > 0) {
      const productToAdd: any = {
        id: this.generateProductId(),
        name: this.newProduct.name,
        description: this.newProduct.description,
        price: this.newProduct.price,
        originalPrice: this.newProduct.originalPrice,
        brand: this.newProduct.brand,
        category: this.newProduct.category,
        stock: this.newProduct.stock,
        collection: this.newProduct.collection,
        gender: this.newProduct.gender,
        imageUrl: this.newProduct.imageUrl || 'assets/images/no-image.png',
        rating: this.newProduct.rating,
        status: this.newProduct.status
      };
      this.adminProductService.addProduct(productToAdd).subscribe({
        next: () => {
          alert('Product added successfully!');
          this.loadProducts();
          this.resetForm();
          this.showAddForm = false;
        },
        error: () => {
          alert('Failed to add product.');
        }
      });
    }
  }

  editProduct(product: ProductListItem) {
    this.editingProduct = { ...product };
    this.showEditForm = true;
  }

  updateProduct() {
    console.log('Editing product:', this.editingProduct);
    console.log('Product ID:', this.editingProduct?.id);
    if (this.editingProduct && this.editingProduct.id && this.editingProduct.id.trim() !== '') {
      // Set stock to 0 if status is OUT_OF_STOCK
      if (this.editingProduct.status === 'OUT_OF_STOCK') {
        this.editingProduct.stock = 0;
      }
      
      const productToUpdate: any = {
        id: this.editingProduct.id,
        name: this.editingProduct.name,
        description: this.editingProduct.description ?? '',
        price: this.editingProduct.price,
        originalPrice: this.editingProduct.originalPrice,
        brand: this.editingProduct.brand,
        category: this.editingProduct.category ?? '',
        stock: this.editingProduct.stock || 0,
        collection: this.editingProduct.collection || '',
        gender: this.editingProduct.gender || '',
        imageUrl: this.editingProduct.imageUrl,
        rating: this.editingProduct.rating,
        status: this.editingProduct.status || 'ACTIVE'
      };
      this.adminProductService.updateProduct(productToUpdate.id, productToUpdate).subscribe({
        next: () => {
          alert('Product updated successfully!');
          this.loadProducts();
          this.showEditForm = false;
          this.editingProduct = null;
        },
        error: () => {
          alert('Failed to update product.');
        }
      });
    } else {
      alert('Invalid product ID. Cannot update product.');
    }
  }

  deleteProduct(id: string) {
    console.log('Deleting product with ID:', id);
    if (!id || id.trim() === '') {
      alert('Invalid product ID. Cannot delete product.');
      return;
    }
    if (confirm('Are you sure you want to delete this product?')) {
      this.adminProductService.deleteProduct(id).subscribe({
        next: () => {
          alert('Product deleted successfully!');
          this.loadProducts();
        },
        error: () => {
          alert('Failed to delete product.');
        }
      });
    }
  }

  resetForm() {
    this.newProduct = {
      name: '',
      category: '',
      collection: '',
      gender: '',
      price: 0,
      originalPrice: 0,
      brand: '',
      stock: 0,
      description: '',
      imageUrl: '',
      rating: 4.0,
      status: 'ACTIVE'
    };
  }

  cancelEdit() {
    this.showEditForm = false;
    this.editingProduct = null;
  }

  onImageError(event: Event) {
    const img = event.target as HTMLImageElement;
    img.src = 'assets/images/no-image.png';
  }

  toNumber(value: any): number {
    return Number(value);
  }

  private generateProductId(): string {
    return 'PROD_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
  }

  getDisplayStatus(status: string | undefined): string {
    switch (status) {
      case 'ACTIVE': return 'Active';
      case 'INACTIVE': return 'Inactive';
      case 'OUT_OF_STOCK': return 'Out of Stock';
      default: return status || 'Unknown';
    }
  }
}
