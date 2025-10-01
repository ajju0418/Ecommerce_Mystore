import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService, Order } from '../../../core/services/order-service';
import { UserService } from '../../../core/services/user-service';
import { User } from '../../../core/models/user.model';

interface CustomerData {
  id: number;
  name: string;
  email: string;
  phone: string;
  status: string;
  joinDate: string;
  orders: number;
  totalSpent: number;
}

@Component({
  selector: 'app-admin-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-customers.html',
  styleUrl: './admin-customers.css'
})
export class AdminCustomers implements OnInit {
  customers: CustomerData[] = [];
  showActionsIndex: number | null = null;

  constructor(
    private orderService: OrderService,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.loadAllCustomersWithStats();
  }

  private loadAllCustomersWithStats() {
    this.userService.getAllUsers().subscribe({
      next: (users: User[]) => {
        this.orderService.getAllOrders().subscribe({
          next: (orders: Order[]) => {
            // Aggregate orders for each customer by userEmail
            const orderStats = new Map<string, { orders: number; totalSpent: number }>();
            (orders || []).forEach((order: Order) => {
              if (!order || !order.userEmail) return;
              const email = order.userEmail;
              if (!orderStats.has(email)) {
                orderStats.set(email, { orders: 0, totalSpent: 0 });
              }
              const stats = orderStats.get(email)!;
              stats.orders += 1;
              stats.totalSpent += order.totalAmount || 0;
            });

            // Build customer list with stats
            this.customers = (users || []).map((user: User) => {
              const stats = orderStats.get(user.email) || { orders: 0, totalSpent: 0 };
              return {
                id: user.id ?? 0,
                name: user.username,
                email: user.email,
                phone: user.phone,
                status: 'Active',
                joinDate: '',
                orders: stats.orders,
                totalSpent: stats.totalSpent
              };
            });
          },
          error: () => {
            // Orders failed: show users without stats
            this.customers = (users || []).map((u: User) => ({
              id: u.id ?? 0,
              name: u.username,
              email: u.email,
              phone: u.phone,
              status: 'Active',
              joinDate: '',
              orders: 0,
              totalSpent: 0
            }));
          }
        });
      },
      error: () => {
        this.customers = [];
      }
    });
  }

  showActions(idx: number) {
    this.showActionsIndex = idx;
  }

  hideActions() {
    this.showActionsIndex = null;
  }

  showEditModal = false;
  editForm: CustomerData = {} as CustomerData;

  editCustomer(customer: CustomerData) {
    this.editForm = { ...customer };
    this.showEditModal = true;
    this.hideActions();
  }

  saveCustomer() {
    const index = this.customers.findIndex(c => c.id === this.editForm.id);
    if (index !== -1) {
      // Map CustomerData to User
      const user: User = {
        id: this.editForm.id,
        username: this.editForm.name,
        email: this.editForm.email,
        phone: this.editForm.phone,
        gender: 'Other', // Default or fetch from elsewhere
        password: '', // Leave blank or fetch from elsewhere
      };
      this.userService.updateUser(user.id!, user).subscribe({
        next: () => {
          this.loadAllCustomersWithStats();
        },
        error: (err) => {
          alert('Failed to update customer: ' + (err?.message || err));
        }
      });
    }
    this.closeEditModal();
  }

  deleteCustomer(customer: CustomerData) {
    if (confirm(`Delete customer ${customer.name}? This will remove all their order history.`)) {
      this.userService.deleteUser(customer.id).subscribe({
        next: () => {
          alert('Customer deleted successfully!');
          this.loadAllCustomersWithStats(); // Refresh list after deletion
        },
        error: (err) => {
          alert('Failed to delete customer: ' + (err?.message || err));
        }
      });
    }
    this.hideActions();
  }

  closeEditModal() {
    this.showEditModal = false;
    this.editForm = {} as CustomerData;
  }

  getActiveCustomers(): number {
    return this.customers.filter(c => c.status === 'Active').length;
  }

  getStatusClass(status: string): string {
    return status === 'Active' ? 'status-active' : 'status-inactive';
  }
}
