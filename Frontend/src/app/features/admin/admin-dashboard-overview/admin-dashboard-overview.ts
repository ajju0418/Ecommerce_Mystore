import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService, Order } from '../../../core/services/order-service';
import { Productservice } from '../../../core/services/productservice';
import { UserService } from '../../../core/services/user-service';


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
  customers: any[] = [];
  stats: any[] = [];
  recentOrders: any[] = [];
  topProducts: any[] = [];
  loading = true;
  error: string | null = null;

  constructor(
    private orderService: OrderService, 
    private productService: Productservice,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.loadAdminData();
  }

  loadAdminData() {
    this.loading = true;
    
    // Load all data concurrently
    Promise.all([
      this.orderService.getAllOrders().toPromise(),
      this.productService.getProducts().toPromise(),
      this.userService.getAllUsers().toPromise()
    ]).then(([orders, products, customers]) => {
      // Map customer information from backend fields
      this.orders = (orders || []).map(order => {
        const orderAny = order as any;
        return {
          ...order,
          customerInfo: {
            name: orderAny.customerName || order.userName || 'N/A',
            email: orderAny.customerEmail || order.userEmail || '',
            phone: orderAny.customerPhone || order.userPhone || '',
            contact: parseInt(orderAny.customerPhone || order.userPhone || '0'),
            address: orderAny.customerAddress || ''
          }
        };
      });
      
      this.products = products || [];
      this.customers = customers || [];
      
      this.updateStats();
      this.updateRecentOrders();
      this.updateTopProducts();
      
      this.loading = false;
      console.log('Loaded admin dashboard data:', {
        orders: this.orders.length,
        products: this.products.length,
        customers: this.customers.length
      });
    }).catch((err: any) => {
      console.error('Failed to load admin data:', err);
      this.error = 'Failed to load dashboard data.';
      this.loading = false;
    });
  }

  private updateStats() {
    const totalSales = this.orders.reduce((sum, order) => sum + order.totalAmount, 0);
    
    // Calculate current month vs previous month
    const now = new Date();
    const currentMonth = now.getMonth();
    const currentYear = now.getFullYear();
    
    const currentMonthOrders = this.orders.filter(order => {
      const orderDate = new Date(order.orderDate);
      return orderDate.getMonth() === currentMonth && orderDate.getFullYear() === currentYear;
    });
    
    const previousMonth = currentMonth === 0 ? 11 : currentMonth - 1;
    const previousYear = currentMonth === 0 ? currentYear - 1 : currentYear;
    
    const previousMonthOrders = this.orders.filter(order => {
      const orderDate = new Date(order.orderDate);
      return orderDate.getMonth() === previousMonth && orderDate.getFullYear() === previousYear;
    });
    
    // Calculate percentages
    const currentSales = currentMonthOrders.reduce((sum, order) => sum + order.totalAmount, 0);
    const previousSales = previousMonthOrders.reduce((sum, order) => sum + order.totalAmount, 0);
    const salesChange = this.calculatePercentageChange(previousSales, currentSales);
    
    const ordersChange = this.calculatePercentageChange(previousMonthOrders.length, currentMonthOrders.length);
    
    // Calculate new customers this month vs last month
    const newCustomersThisMonth = this.customers.filter(customer => {
      if (!customer.createdAt) return false;
      const customerDate = new Date(customer.createdAt);
      return customerDate.getMonth() === currentMonth && customerDate.getFullYear() === currentYear;
    }).length;
    
    const newCustomersLastMonth = this.customers.filter(customer => {
      if (!customer.createdAt) return false;
      const customerDate = new Date(customer.createdAt);
      return customerDate.getMonth() === previousMonth && customerDate.getFullYear() === previousYear;
    }).length;
    
    const customersChange = this.calculatePercentageChange(newCustomersLastMonth, newCustomersThisMonth);
    
    // Calculate active products (products that were ordered this month)
    const activeProductsThisMonth = new Set();
    const activeProductsLastMonth = new Set();
    
    currentMonthOrders.forEach(order => {
      order.items.forEach((item: any) => activeProductsThisMonth.add(item.id));
    });
    
    previousMonthOrders.forEach(order => {
      order.items.forEach((item: any) => activeProductsLastMonth.add(item.id));
    });
    
    const productsChange = this.calculatePercentageChange(activeProductsLastMonth.size, activeProductsThisMonth.size);
    
    this.stats = [
      { title: 'Total Sales', value: `₹${totalSales.toLocaleString()}`, change: salesChange, icon: '💰', color: 'bg-green-500' },
      { title: 'Total Orders', value: this.orders.length.toString(), change: ordersChange, icon: '📦', color: 'bg-blue-500' },
      { title: 'Total Customers', value: this.customers.length.toString(), change: customersChange, icon: '👥', color: 'bg-purple-500' },
      { title: 'Total Products', value: this.products.length.toString(), change: productsChange, icon: '🏷️', color: 'bg-orange-500' }
    ];
  }
  
  private calculatePercentageChange(previous: number, current: number): string {
    if (previous === 0) {
      return current > 0 ? '+100%' : '0%';
    }
    const change = ((current - previous) / previous) * 100;
    const sign = change >= 0 ? '+' : '';
    return `${sign}${change.toFixed(1)}%`;
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
