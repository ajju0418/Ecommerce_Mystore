import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService, Order } from '../../../core/services/order-service';
import { UserService } from '../../../core/services/user-service';
import { Header } from '../../../layout/header/header';
import { Footer } from '../../../layout/footer/footer';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule, DatePipe, Header, Footer],
  templateUrl: './order-details.html',
  styleUrls: ['./order-details.css']
})
export class OrderDetailsComponent implements OnInit {

  order: Order | undefined;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const currentUser = this.userService.getCurrentUser();
    if (!currentUser) {
      alert('Please login to view order details.');
      this.router.navigate(['/login']);
      return;
    }

    const orderId = this.route.snapshot.paramMap.get('id');
    if (orderId) {
      this.loadOrderDetails(orderId);
    } else {
      this.error = 'Invalid order ID';
      this.loading = false;
    }
  }

  loadOrderDetails(orderId: string): void {
    this.loading = true;
    this.error = null;
    this.orderService.fetchOrderById(orderId).subscribe({
      next: (order) => {
        this.order = order;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load order details.';
        this.loading = false;
      }
    });
  }

  retryLoad(): void {
    const orderId = this.route.snapshot.paramMap.get('id');
    if (orderId) {
      this.loadOrderDetails(orderId);
    }
  }

  goBack(): void {
    this.router.navigate(['/order-history']);
  }

  onImageError(event: Event): void {
    (event.target as HTMLImageElement).src = 'assets/default.jpg';
  }

  getStatusClass(status: string): string {
    switch (status?.toLowerCase()) {
      case 'completed':
      case 'delivered': 
        return 'bg-green-100 text-green-800';
      case 'processing': 
        return 'bg-blue-100 text-blue-800';
      case 'pending': 
        return 'bg-yellow-100 text-yellow-800';
      case 'cancelled': 
        return 'bg-red-100 text-red-800';
      default: 
        return 'bg-gray-100 text-gray-800';
    }
  }
}
