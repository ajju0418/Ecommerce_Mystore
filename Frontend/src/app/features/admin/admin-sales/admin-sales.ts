import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService, Order } from '../../../core/services/order-service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-admin-sales',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-sales.html',
  styleUrl: './admin-sales.css'
})
export class AdminSales implements OnInit, OnDestroy {
  orders: Order[] = [];
  stats = {
    ordersChange: '+0%',
    revenueChange: '+0%',
    completedChange: '+0%',
    avgOrderChange: '+0%'
  };
  private subscription = new Subscription();

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    // Subscribe to orders observable for reactive updates
    this.subscription.add(
      this.orderService.orders$.subscribe(orders => {
        if (orders.length > 0) {
          this.orders = orders.map(order => ({
            ...order,
            customerInfo: order.customerInfo || {
              name: (order as any).customerName || (order as any).userName || order.userName || `Customer ${(order as any).userId || order.userId || 'Unknown'}`,
              email: (order as any).customerEmail || (order as any).userEmail || order.userEmail || '',
              phone: (order as any).customerPhone || (order as any).userPhone || order.userPhone || '',
              contact: parseInt((order as any).customerPhone || (order as any).userPhone || order.userPhone || '0'),
              address: (order as any).customerAddress || ''
            }
          }));
          this.updateStats();
        }
      })
    );
    this.loadOrders();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  loadOrders() {
    this.subscription.add(
      this.orderService.getAllOrders().subscribe({
        next: (orders: Order[]) => {
          this.orders = (orders || []).map(order => {
            const orderAny = order as any;
            return {
              ...order,
              customerInfo: {
                name: orderAny.customerName || orderAny.userName || order.userName || `Customer ${orderAny.userId || order.userId || 'Unknown'}`,
                email: orderAny.customerEmail || orderAny.userEmail || order.userEmail || '',
                phone: orderAny.customerPhone || orderAny.userPhone || order.userPhone || '',
                contact: parseInt(orderAny.customerPhone || orderAny.userPhone || order.userPhone || '0'),
                address: orderAny.customerAddress || ''
              }
            };
          });
          this.updateStats();
        },
        error: (error: any) => {
          console.error('Failed to load orders:', error);
          this.orders = [];
        }
      })
    );
  }

  formatDate(date: string | Date): string {
    if (!date) return 'N/A';
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day}-${month}-${year}`;
  }

  getStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'completed': return 'bg-green-100 text-green-800';
      case 'processing': return 'bg-yellow-100 text-yellow-800';
      case 'pending': return 'bg-gray-100 text-gray-800';
      case 'cancelled': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getTotalSales(): number {
    return this.orders.reduce((total, order) => total + order.totalAmount, 0);
  }

  getCompletedOrders(): number {
    return this.orders.filter(order => order.status?.toLowerCase() === 'completed').length;
  }

  getAverageOrderValue(): number {
    if (this.orders.length === 0) return 0;
    const total = this.getTotalSales();
    return total / this.orders.length;
  }

  getTodaysOrders(): number {
    const today = new Date().toDateString();
    return this.orders.filter(order =>
      new Date(order.orderDate).toDateString() === today
    ).length;
  }

  acceptOrder(order: Order) {
    if (confirm(`Accept order ${order.id}?`) && order.id) {
      this.orderService.updateOrderStatus(order.id, 'PROCESSING').subscribe({
        next: (response) => {
          if (response.success) {
            alert('Order accepted successfully!');
          } else {
            alert('Failed to accept order: ' + response.message);
          }
        },
        error: (error: any) => {
          console.error('Failed to update order status:', error);
          alert('Error accepting order');
        }
      });
    }
  }

  rejectOrder(order: Order) {
    if (confirm(`Reject order ${order.id}?`) && order.id) {
      this.orderService.updateOrderStatus(order.id, 'CANCELLED').subscribe({
        next: (response) => {
          if (response.success) {
            alert('Order rejected successfully!');
          } else {
            alert('Failed to reject order: ' + response.message);
          }
        },
        error: (error: any) => {
          console.error('Failed to update order status:', error);
          alert('Error rejecting order');
        }
      });
    }
  }

  completeOrder(order: Order) {
    console.log('=== ADMIN SALES COMPLETE ORDER DEBUG ===');
    console.log('AdminSales: Complete order called for:', order);
    console.log('AdminSales: Order ID being used:', order.id);
    console.log('AdminSales: Order object keys:', Object.keys(order));
    
    if (confirm(`Mark order ${order.id} as completed?`) && order.id) {
      this.orderService.updateOrderStatus(order.id, 'COMPLETED').subscribe({
        next: (response) => {
          if (response.success) {
            alert('Order marked as completed!');
          } else {
            alert('Failed to update order: ' + response.message);
            console.error('Update order response:', response);
          }
        },
        error: (error: any) => {
          if (error?.message?.includes('400') || error?.message?.includes('Bad Request') || error?.message?.includes('not found')) {
            alert('Order not found in database.');
          } else {
            let msg = error?.error?.message || error?.message || 'Failed to update order status';
            alert('Error: ' + msg);
          }
          console.error('Failed to update order status:', error);
        }
      });
    }
  }

  // Update deleteOrder to use correct backend endpoint
  deleteOrder(order: Order) {
    if (confirm(`Delete order ${order.id}? This cannot be undone.`) && order.id) {
      this.orderService.deleteOrder(order.id).subscribe({
        next: () => {
          alert('Order deleted successfully!');
          this.loadOrders();
        },
        error: (error: any) => {
          console.error('Failed to delete order:', error);
          alert('Error deleting order');
        }
      });
    }
  }

  getPendingOrdersCount(): number {
    return this.orders.filter(order => order.status === 'pending').length;
  }

  getPaymentStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'delivered': return 'status-completed';
      case 'completed': return 'status-completed';
      case 'processing': return 'status-processing';
      case 'pending': return 'bg-yellow-100 text-yellow-800';
      case 'failed': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  private updateStats() {
    const now = new Date();
    const currentMonth = now.getMonth();
    const currentYear = now.getFullYear();
    const lastMonth = currentMonth === 0 ? 11 : currentMonth - 1;
    const lastMonthYear = currentMonth === 0 ? currentYear - 1 : currentYear;

    const currentMonthOrders = this.orders.filter(order => {
      const orderDate = new Date(order.orderDate);
      return orderDate.getMonth() === currentMonth && orderDate.getFullYear() === currentYear;
    });

    const lastMonthOrders = this.orders.filter(order => {
      const orderDate = new Date(order.orderDate);
      return orderDate.getMonth() === lastMonth && orderDate.getFullYear() === lastMonthYear;
    });

    // Calculate changes
    const ordersChange = this.calculatePercentageChange(lastMonthOrders.length, currentMonthOrders.length);
    const currentRevenue = currentMonthOrders.reduce((sum, order) => sum + order.totalAmount, 0);
    const lastRevenue = lastMonthOrders.reduce((sum, order) => sum + order.totalAmount, 0);
    const revenueChange = this.calculatePercentageChange(lastRevenue, currentRevenue);
    
    const currentCompleted = currentMonthOrders.filter(order => order.status?.toLowerCase() === 'completed').length;
    const lastCompleted = lastMonthOrders.filter(order => order.status?.toLowerCase() === 'completed').length;
    const completedChange = this.calculatePercentageChange(lastCompleted, currentCompleted);
    
    const currentAvg = currentMonthOrders.length > 0 ? currentRevenue / currentMonthOrders.length : 0;
    const lastAvg = lastMonthOrders.length > 0 ? lastRevenue / lastMonthOrders.length : 0;
    const avgOrderChange = this.calculatePercentageChange(lastAvg, currentAvg);

    this.stats = {
      ordersChange,
      revenueChange,
      completedChange,
      avgOrderChange
    };
  }

  private calculatePercentageChange(oldValue: number, newValue: number): string {
    if (oldValue === 0) return newValue > 0 ? '+100%' : '0%';
    const change = ((newValue - oldValue) / oldValue) * 100;
    const sign = change >= 0 ? '+' : '';
    return `${sign}${Math.round(change)}%`;
  }


}
