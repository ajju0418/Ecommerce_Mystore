import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Productservice as AdminProductService, Product } from '../../../core/services/productservice';
import { ProductListItem } from '../../../core/models/product.types';

interface ProductPage {
  content: Product[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

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
    type: '',
    collection: '',
    gender: '',
    price: 0,
    stock: 0,
    description: '',
    imageUrl: '',
    rating: 4.0,
    status: 'ACTIVE'
  };
  categories = ['Shirts', 'T-Shirts', 'Jeans', 'Jackets', 'Shoes', 'Accessories', 'Dresses', 'Pants', 'Sweaters', 'Hoodies'];
  collections = ['Deal of the Day', 'New Arrivals', 'Sale', 'Premium', 'Casual', 'Sports'];

  constructor(

    private adminProductService: AdminProductService
  ) {}

  ngOnInit() {
    // Always load products from backend database
    this.loadProducts();
  }

  private mapProductToListItem(product: Product): ProductListItem {
    return {
      id: product.id && !isNaN(Number(product.id)) ? String(product.id) : '', // Ensure id is a valid string
      name: product.name,
      price: product.price,
      imageUrl: product.imageUrl ?? 'https://via.placeholder.com/48x48?text=No+Image',
      rating: product.rating ?? 0,
      category: product.category,
      description: product.description,
      stock: product.stockQuantity,
      status: (product as any).status, // Status from backend
      collection: product.collection,
      type: product.type,
      gender: product.gender
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
    // Filter products from backend response only
    if (this.selectedCategory) {
      // TODO: Implement getProductsByCategory in service
      this.adminProductService.getProducts().subscribe({
        next: (products: Product[]) => {
          const listItems = products.filter(p => p.category === this.selectedCategory).map(this.mapProductToListItem);
          this.filteredProducts = listItems.filter((product: ProductListItem) => {
            const matchesSearch =
              !this.searchTerm ||
              product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
              (product.type && product.type.toLowerCase().includes(this.searchTerm.toLowerCase())) ||
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
          (product.type && product.type.toLowerCase().includes(this.searchTerm.toLowerCase())) ||
          (product.collection && product.collection.toLowerCase().includes(this.searchTerm.toLowerCase()));
        const matchesStatus = !this.selectedStatus || product.status === this.selectedStatus;
        return matchesSearch && matchesStatus;
      });
    }
  }

  addProduct() {
    // Add product to backend database
    if (this.newProduct.name && this.newProduct.collection && this.newProduct.price > 0) {
      const productToAdd: Product = {
        name: this.newProduct.name,
        description: this.newProduct.description,
        price: this.newProduct.price,
        category: this.newProduct.type, // Assuming type maps to category
        stockQuantity: this.newProduct.stock,
        collection: this.newProduct.collection,
        gender: this.newProduct.gender,
        imageUrl: this.newProduct.imageUrl,
        rating: this.newProduct.rating
      };
      this.adminProductService.createProduct(productToAdd).subscribe({
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
    if (this.editingProduct && this.editingProduct.id && !isNaN(Number(this.editingProduct.id))) {
      const productToUpdate: Product = {
        name: this.editingProduct.name,
        description: this.editingProduct.description ?? '',
        price: this.editingProduct.price,
        category: this.editingProduct.category ?? '',
        stockQuantity: this.editingProduct.stock || 0,
        collection: this.editingProduct.collection || '',
        gender: this.editingProduct.gender || '',
        imageUrl: this.editingProduct.imageUrl,
        rating: this.editingProduct.rating
      };
      this.adminProductService.updateProduct(Number(this.editingProduct.id), productToUpdate).subscribe({
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

  deleteProduct(id: number) {
    if (!id || isNaN(Number(id))) {
      alert('Invalid product ID. Cannot delete product.');
      return;
    }
    if (confirm('Are you sure you want to delete this product?')) {
      this.adminProductService.deleteProduct(Number(id)).subscribe({
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
      type: '',
      collection: '',
      gender: '',
      price: 0,
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
    img.src = 'https://via.placeholder.com/48x48?text=No+Image';
  }

  toNumber(value: any): number {
    return Number(value);
  }
}
