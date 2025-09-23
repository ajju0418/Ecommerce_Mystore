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
    this.userService.getAllUsers().subscribe((users: User[]) => {
      this.orderService.getOrders().subscribe((orders: Order[]) => {
        const customerMap = new Map<string, CustomerData>();
        users.forEach((user: User) => {
          customerMap.set(user.email, {
            id: user.id ?? 0,
            name: user.username,
            email: user.email,
            phone: user.phone,
            status: 'Active',
            joinDate: '', // You can add join date if available in user model
            orders: 0,
            totalSpent: 0
          });
        });
        orders.forEach((order: Order) => {
          if (!order || !order.customerInfo || !order.customerInfo.email) return;
          const key = order.customerInfo.email;
          if (customerMap.has(key)) {
            const customer = customerMap.get(key)!;
            customer.orders += 1;
            customer.totalSpent += order.totalAmount;
          }
        });
        this.customers = Array.from(customerMap.values());
      });
    });
  }

  showActions(idx: number) {
    this.showActionsIndex = idx;
  }

  hideActions() {
    this.showActionsIndex = null;
  }

  selectedCustomer: CustomerData | null = null;
  showDetailsModal = false;
  showEditModal = false;
  editForm: CustomerData = {} as CustomerData;

  viewDetails(customer: CustomerData) {
    this.selectedCustomer = customer;
    this.showDetailsModal = true;
    this.hideActions();
  }

  editCustomer(customer: CustomerData) {
    this.editForm = { ...customer };
    this.showEditModal = true;
    this.hideActions();
  }

  saveCustomer() {
    const index = this.customers.findIndex(c => c.id === this.editForm.id);
    if (index !== -1) {
      this.customers[index] = { ...this.editForm };
    }
    this.closeEditModal();
  }

  deleteCustomer(customer: CustomerData) {
    if (confirm(`Delete customer ${customer.name}? This will remove all their order history.`)) {
      this.customers = this.customers.filter(c => c.id !== customer.id);

      this.orderService.getOrders().subscribe((orders: Order[]) => {
        const filteredOrders = orders.filter((order: Order) => order.customerInfo.email !== customer.email);
        localStorage.setItem('orders', JSON.stringify(filteredOrders));
        window.location.reload();
      });
    }
    this.hideActions();
  }

  closeDetailsModal() {
    this.showDetailsModal = false;
    this.selectedCustomer = null;
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
