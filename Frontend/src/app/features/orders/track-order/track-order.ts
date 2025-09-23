import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService, Order } from '../../../core/services/order-service';
import { UserService } from '../../../core/services/user-service';

@Component({
  selector: 'app-track-order',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './track-order.html',
  styleUrls: ['./track-order.css']
})
export class TrackOrderComponent implements OnInit {
  orderId: string | null = null;
  order: Order | null = null;
  trackingStatus: string = 'pending';
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getCurrentUser();
    if (!currentUser) {
      alert('Please login to track your order.');
      this.router.navigate(['/login']);
      return;
    }

    this.orderId = this.route.snapshot.paramMap.get('id');
    if (this.orderId) {
      this.loadOrderDetails();
    } else {
      this.error = 'Invalid order ID';
      this.loading = false;
    }
  }

  loadOrderDetails() {
    if (!this.orderId) return;
    
    this.loading = true;
    this.error = null;
    
    this.orderService.getOrderDetails(this.orderId).subscribe({
      next: (response) => {
        if (response.success) {
          this.order = response.order;
          if (this.order) {
            this.trackingStatus = this.order.status;
          }
        } else {
          this.error = response.message || 'Order not found';
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load order details:', error);
        this.error = 'Failed to load order details. Please try again.';
        this.loading = false;
      }
    });
  }

  retryLoad(): void {
    this.loadOrderDetails();
  }

  goBack(): void {
    this.router.navigate(['/order-history']);
  }

  isStepCompleted(step: string): boolean {
    const statusMap: { [key: string]: number } = {
      'pending': 0, 'PENDING': 0,
      'paid': 1, 'PAID': 1,
      'confirmed': 1, 'CONFIRMED': 1,
      'processing': 1, 'PROCESSING': 1,
      'shipped': 2, 'SHIPPED': 2,
      'delivered': 2, 'DELIVERED': 2,
      'completed': 2, 'COMPLETED': 2
    };
    
    const stepMap: { [key: string]: number } = {
      'pending': 0,
      'processing': 1,
      'completed': 2
    };
    
    const currentLevel = statusMap[this.trackingStatus] ?? -1;
    const stepLevel = stepMap[step] ?? -1;
    
    return stepLevel <= currentLevel && !['cancelled', 'CANCELLED', 'failed', 'FAILED'].includes(this.trackingStatus);
  }

  getStatusMessage(): string {
    switch (this.trackingStatus?.toUpperCase()) {
      case 'PENDING': return 'Order placed and waiting for confirmation';
      case 'PAID': return 'Payment confirmed, order will be processed soon';
      case 'CONFIRMED': return 'Order confirmed and accepted';
      case 'PROCESSING': return 'Order is being prepared for shipment';
      case 'SHIPPED': return 'Order has been shipped and is on the way';
      case 'DELIVERED': return 'Order delivered successfully';
      case 'COMPLETED': return 'Order completed successfully';
      case 'CANCELLED': return 'Order has been cancelled';
      case 'FAILED': return 'Order processing failed';
      default: return 'Order status unknown';
    }
  }
}
