import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { OrderService, Order } from '../../../core/services/order-service';
import { UserService } from '../../../core/services/user-service';
import { User } from '../../../core/models/user.model';
import { Header } from '../../../layout/header/header';
import { Footer } from '../../../layout/footer/footer';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-order-history',
  standalone: true,
  imports: [CommonModule, DatePipe, Header, Footer],
  templateUrl: './order-history.html',
  styleUrls: ['./order-history.css']
})
export class OrderHistoryComponent implements OnInit, OnDestroy {

  orders: Order[] = [];
  filteredOrders: Order[] = [];
  selectedFilter: string = 'all';
  currentUser: User | null = null;
  loading = true;
  error: string | null = null;
  private ordersSubscription!: Subscription;

  constructor(
    private orderService: OrderService, 
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    if (!this.currentUser) {
      alert('Please login to view your order history.');
      this.router.navigate(['/login']);
      return;
    }

    this.loadUserOrders();
  }

  loadUserOrders(): void {
    if (!this.currentUser?.id) return;
    
    this.loading = true;
    this.error = null;
    
    // Load user orders from backend
    this.orderService.loadUserOrders(this.currentUser.id);
    
    // Subscribe to the orders$ observable to get all orders in real-time
    this.ordersSubscription = this.orderService.getOrders().subscribe({
      next: (allOrders) => {
        this.orders = allOrders;
        this.applyFilter();
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load orders:', error);
        this.error = 'Failed to load order history. Please try again.';
        this.loading = false;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.ordersSubscription) {
      this.ordersSubscription.unsubscribe();
    }
  }

  // Method to view the order details page
  viewOrderDetails(orderId: string): void {
    this.router.navigate(['/order-details', orderId]);
  }

  // Method to cancel an order
  cancelOrder(orderId: string): void {
    if (confirm('Are you sure you want to cancel this order?')) {
      this.orderService.cancelOrder(orderId).subscribe({
        next: (response) => {
          if (response.success) {
            alert('Order cancelled successfully');
            this.loadUserOrders(); // Refresh the orders
            // Update the filter to show the change immediately
            setTimeout(() => this.applyFilter(), 100);
          } else {
            alert(response.message || 'Failed to cancel order');
          }
        },
        error: (error) => {
          console.error('Cancel order error:', error);
          alert('Failed to cancel order');
        }
      });
    }
  }

  // Method to retry loading orders
  retryLoad(): void {
    this.loadUserOrders();
  }

  // Method to view the track order page
  trackOrder(orderId: string): void {
    this.router.navigate(['/track-order', orderId]);
  }

  // Method to view payment details
  viewPaymentDetails(order: Order): void {
    if (order.paymentDetails) {
      const details = `Payment Details:\n\nTransaction ID: ${order.paymentDetails.transactionId}\nMethod: ${order.paymentDetails.method}\nStatus: ${order.paymentDetails.status}\nTimestamp: ${new Date(order.paymentDetails.timestamp).toLocaleString()}`;
      alert(details);
    }
  }

  // Helper method to get display text for order status
  getStatusDisplayText(status: string): string {
    const statusMap: { [key: string]: string } = {
      'pending': 'Order Placed',
      'PENDING': 'Order Placed',
      'paid': 'Payment Confirmed',
      'PAID': 'Payment Confirmed',
      'confirmed': 'Order Accepted',
      'CONFIRMED': 'Order Accepted',
      'processing': 'Being Prepared',
      'PROCESSING': 'Being Prepared',
      'shipped': 'Shipped',
      'SHIPPED': 'Shipped',
      'delivered': 'Delivered',
      'DELIVERED': 'Delivered',
      'completed': 'Completed',
      'COMPLETED': 'Completed',
      'cancelled': 'Cancelled',
      'CANCELLED': 'Cancelled',
      'failed': 'Failed',
      'FAILED': 'Failed'
    };
    return statusMap[status] || status.charAt(0).toUpperCase() + status.slice(1).toLowerCase();
  }

  // Helper method to check if order is cancelled
  isOrderCancelled(status: string): boolean {
    return ['cancelled', 'CANCELLED', 'failed', 'FAILED'].includes(status);
  }

  // Helper method to check if order can be cancelled
  canCancelOrder(status: string): boolean {
    return ['pending', 'PENDING', 'paid', 'PAID', 'confirmed', 'CONFIRMED'].includes(status);
  }

  // Helper method to check if order can be reordered
  canReorder(status: string): boolean {
    return ['delivered', 'DELIVERED', 'completed', 'COMPLETED'].includes(status);
  }

  // Method to reorder items
  reorderItems(order: Order): void {
    if (confirm('Add these items to your cart again?')) {
      // Navigate to cart or product pages with the items
      // This would typically involve adding items back to cart
      alert('Items will be added to your cart. This feature can be implemented to add items to cart service.');
    }
  }

  // Filter methods
  filterByStatus(filter: string): void {
    this.selectedFilter = filter;
    this.applyFilter();
  }

  private applyFilter(): void {
    switch (this.selectedFilter) {
      case 'active':
        this.filteredOrders = this.orders.filter(order => 
          ['pending', 'PENDING', 'paid', 'PAID', 'confirmed', 'CONFIRMED', 'processing', 'PROCESSING', 'shipped', 'SHIPPED'].includes(order.status)
        );
        break;
      case 'completed':
        this.filteredOrders = this.orders.filter(order => 
          ['delivered', 'DELIVERED', 'completed', 'COMPLETED'].includes(order.status)
        );
        break;
      case 'cancelled':
        this.filteredOrders = this.orders.filter(order => 
          ['cancelled', 'CANCELLED', 'failed', 'FAILED'].includes(order.status)
        );
        break;
      default:
        this.filteredOrders = [...this.orders];
    }
  }

  // Count methods for filter buttons
  getActiveOrdersCount(): number {
    return this.orders.filter(order => 
      ['pending', 'PENDING', 'paid', 'PAID', 'confirmed', 'CONFIRMED', 'processing', 'PROCESSING', 'shipped', 'SHIPPED'].includes(order.status)
    ).length;
  }

  getCompletedOrdersCount(): number {
    return this.orders.filter(order => 
      ['delivered', 'DELIVERED', 'completed', 'COMPLETED'].includes(order.status)
    ).length;
  }

  getCancelledOrdersCount(): number {
    return this.orders.filter(order => 
      ['cancelled', 'CANCELLED', 'failed', 'FAILED'].includes(order.status)
    ).length;
  }

  // Helper method to get status description
  getStatusDescription(status: string): string {
    const descriptionMap: { [key: string]: string } = {
      'pending': 'Your order has been placed and is awaiting confirmation.',
      'PENDING': 'Your order has been placed and is awaiting confirmation.',
      'paid': 'Payment received. Order will be processed soon.',
      'PAID': 'Payment received. Order will be processed soon.',
      'confirmed': 'Order accepted and will be prepared for shipping.',
      'CONFIRMED': 'Order accepted and will be prepared for shipping.',
      'processing': 'Your order is being prepared for shipment.',
      'PROCESSING': 'Your order is being prepared for shipment.',
      'shipped': 'Your order is on its way to you.',
      'SHIPPED': 'Your order is on its way to you.',
      'delivered': 'Order has been successfully delivered.',
      'DELIVERED': 'Order has been successfully delivered.',
      'completed': 'Order completed successfully.',
      'COMPLETED': 'Order completed successfully.',
      'cancelled': 'This order has been cancelled.',
      'CANCELLED': 'This order has been cancelled.',
      'failed': 'Order processing failed. Please contact support.',
      'FAILED': 'Order processing failed. Please contact support.'
    };
    return descriptionMap[status] || 'Status information not available.';
  }
}