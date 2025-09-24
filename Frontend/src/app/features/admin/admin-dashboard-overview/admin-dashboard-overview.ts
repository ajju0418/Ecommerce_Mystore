import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService, Order } from '../../../core/services/order-service';


@Component({
  selector: 'app-admin-dashboard-overview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard-overview.html',
  styleUrl: './admin-dashboard-overview.css'
})
export class AdminDashboardOverview implements OnInit {
  orders: Order[] = [];
  products: any[] = [];
  stats: any[] = [];
  recentOrders: any[] = [];
  topProducts: any[] = [];
  loading = true;
  error: string | null = null;

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.loadAdminData();
  }

  loadAdminData() {
    this.loading = true;
    this.orderService.getAllOrders().subscribe({
      next: (orders: Order[]) => {
        this.orders = orders;
        this.loading = false;
        this.updateStats();
        this.updateRecentOrders();
        this.updateTopProducts();
        console.log('Loaded admin dashboard data:', orders);
      },
      error: (err: any) => {
        console.error('Failed to load admin data:', err);
        this.error = 'Failed to load dashboard data.';
        this.loading = false;
        // Fallback to mock data if service fails
        this.orders = this.getMockOrders();
        this.products = this.getMockProducts();
        this.updateStats();
        this.updateRecentOrders();
        this.updateTopProducts();
      }
    });
  }

  private getMockOrders(): Order[] {
    return [
      {
        id: '1',
        customerInfo: { 
          name: 'John Doe', 
          email: 'john@example.com',
          phone: '123-456-7890',
          contact: 1234567890,
          address: '123 Main St, City, State 12345'
        },
        items: [{ 
          id: '1',
          name: 'Nike Air Max', 
          quantity: 1, 
          price: 150,
          imageUrl: 'assets/images/nike-air-max.jpg',
          rating: 4.5
        }],
        totalAmount: 150,
        orderDate: new Date(),
        status: 'delivered'
      },
      {
        id: '2',
        customerInfo: { 
          name: 'Jane Smith', 
          email: 'jane@example.com',
          phone: '234-567-8901',
          contact: 2345678901,
          address: '456 Oak Ave, City, State 23456'
        },
        items: [{ 
          id: '2',
          name: 'Adidas Ultraboost', 
          quantity: 2, 
          price: 180,
          imageUrl: 'assets/images/adidas-ultraboost.jpg',
          rating: 4.7
        }],
        totalAmount: 360,
        orderDate: new Date(Date.now() - 86400000),
        status: 'pending'
      },
      {
        id: '3',
        customerInfo: { 
          name: 'Mike Johnson', 
          email: 'mike@example.com',
          phone: '345-678-9012',
          contact: 3456789012,
          address: '789 Pine Rd, City, State 34567'
        },
        items: [{ 
          id: '3',
          name: 'Puma Classic', 
          quantity: 1, 
          price: 120,
          imageUrl: 'assets/images/puma-classic.jpg',
          rating: 4.2
        }],
        totalAmount: 120,
        orderDate: new Date(Date.now() - 172800000),
        status: 'processing'
      },
      {
        id: '4',
        customerInfo: { 
          name: 'Sarah Wilson', 
          email: 'sarah@example.com',
          phone: '456-789-0123',
          contact: 4567890123,
          address: '321 Elm St, City, State 45678'
        },
        items: [{ 
          id: '4',
          name: 'Under Armour Shoes', 
          quantity: 1, 
          price: 200,
          imageUrl: 'assets/images/under-armour.jpg',
          rating: 4.6
        }],
        totalAmount: 200,
        orderDate: new Date(Date.now() - 259200000),
        status: 'completed'
      }
    ];
  }

  private getMockProducts(): any[] {
    return [
      { id: 1, name: 'Nike Air Max', price: 150, stock: 50 },
      { id: 2, name: 'Adidas Ultraboost', price: 180, stock: 30 },
      { id: 3, name: 'Puma Classic', price: 120, stock: 25 },
      { id: 4, name: 'Under Armour Shoes', price: 200, stock: 15 }
    ];
  }

  private updateStats() {
    const totalSales = this.orders.reduce((sum, order) => sum + order.totalAmount, 0);
    const uniqueCustomers = new Set(this.orders.map(order => order.customerInfo.email)).size;

    this.stats = [
      { title: 'Total Sales', value: `₹${totalSales.toLocaleString()}`, change: '+12.5%', icon: '💰', color: 'bg-green-500' },
      { title: 'Total Orders', value: this.orders.length.toString(), change: '+8.2%', icon: '📦', color: 'bg-blue-500' },
      { title: 'Total Customers', value: uniqueCustomers.toString(), change: '+15.3%', icon: '👥', color: 'bg-purple-500' },
      { title: 'Total Products', value: this.products.length.toString(), change: '+5.7%', icon: '🏷️', color: 'bg-orange-500' }
    ];
  }

  private updateRecentOrders() {
    this.recentOrders = this.orders
      .sort((a, b) => new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime())
      .slice(0, 4)
      .map(order => ({
        id: order.id,
        customer: order.customerInfo.name,
        product: order.items[0]?.name || 'Multiple Items',
        amount: `₹${order.totalAmount.toLocaleString()}`,
        status: order.status.charAt(0).toUpperCase() + order.status.slice(1)
      }));
  }

  private updateTopProducts() {
    const productSales = new Map();

    this.orders.forEach(order => {
      order.items.forEach((item: any) => {
        if (productSales.has(item.name)) {
          const existing = productSales.get(item.name);
          existing.sales += item.quantity;
          existing.revenue += item.price * item.quantity;
        } else {
          productSales.set(item.name, {
            name: item.name,
            sales: item.quantity,
            revenue: item.price * item.quantity
          });
        }
      });
    });

    this.topProducts = Array.from(productSales.values())
      .sort((a, b) => b.revenue - a.revenue)
      .slice(0, 4)
      .map(product => ({
        ...product,
        revenue: `₹${product.revenue.toLocaleString()}`
      }));
  }
}
