import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService, Order } from '../../../core/services/order-service';

@Component({
  selector: 'app-admin-sales',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-sales.html',
  styleUrl: './admin-sales.css'
})
export class AdminSales implements OnInit {
  orders: Order[] = [];

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService.getAllOrders().subscribe({
      next: (orders: Order[]) => {
        // Map customer information from backend fields
        this.orders = (orders || []).map(order => {
          const orderAny = order as any;
          console.log('=== ADMIN SALES LOAD ORDERS DEBUG ===');
          console.log('AdminSales: Raw order from backend:', orderAny);
          console.log('AdminSales: order.orderId:', orderAny.orderId);
          console.log('AdminSales: order.id:', orderAny.id);
          console.log('AdminSales: order.customerName:', orderAny.customerName);
          console.log('AdminSales: order.userName:', orderAny.userName);
          console.log('AdminSales: order.userId:', orderAny.userId);
          console.log('AdminSales: Final mapped order.id:', orderAny.orderId || orderAny.id);
          
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
      },
      error: (error: any) => {
        console.error('Failed to load orders:', error);
        this.orders = [];
      }
    });
  }

  formatDate(date: string | Date): string {
    if (!date) return 'N/A';
    return new Date(date).toLocaleDateString();
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
        next: () => this.loadOrders(),
        error: (error: any) => console.error('Failed to update order status:', error)
      });
    }
  }

  rejectOrder(order: Order) {
    if (confirm(`Reject order ${order.id}?`) && order.id) {
      this.orderService.updateOrderStatus(order.id, 'CANCELLED').subscribe({
        next: () => this.loadOrders(),
        error: (error: any) => console.error('Failed to update order status:', error)
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
            this.loadOrders();
          } else {
            alert('Failed to update order: ' + response.message);
            console.error('Update order response:', response);
          }
        },
        error: (error: any) => {
          if (error?.message?.includes('400') || error?.message?.includes('Bad Request') || error?.message?.includes('not found')) {
            alert('Order not found in database. Refreshing order list...');
            this.loadOrders();
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
        next: () => this.loadOrders(),
        error: (error: any) => console.error('Failed to delete order:', error)
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
}
