import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderService } from '../../../core/services/order-service';
import { UserService } from '../../../core/services/user-service';
import { ProductListItem } from '../../../core/models/product.types';
import { User } from '../../../core/models/user.model';
import { Footer } from '../../../layout/footer/footer';
import { Header } from '../../../layout/header/header';
import { CanComponentDeactivate } from '../../../core/guards/unsaved-changes.guard';

@Component({
  selector: 'app-order-summary',
  standalone: true,
  imports: [CommonModule, FormsModule, Footer, Header],
  templateUrl: './order-summary.html',
  styleUrls: ['./order-summary.css']
})
export class OrderSummaryComponent implements OnInit, CanComponentDeactivate {

  orderDetails: ProductListItem[] = [];
  totalAmount: number = 0;
  customerName: string = '';
  contactInfo: string = '';
  shippingAddress: string = '';
  isFormSubmitted: boolean = false;
  currentUser: User | null = null;
  loading = false;

  constructor(
    private orderService: OrderService, 
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    if (!this.currentUser) {
      alert('Please login to continue with checkout.');
      this.router.navigate(['/login']);
      return;
    }

    // Pre-fill customer info from user data
    this.customerName = this.currentUser.username;
    this.contactInfo = this.currentUser.phone;

    const preCheckoutOrder = this.orderService.getAndClearPreCheckoutOrder();
    if (preCheckoutOrder) {
      this.orderDetails = preCheckoutOrder.items;
      this.totalAmount = preCheckoutOrder.totalAmount;
    } else {
      alert('No order to display. Redirecting to cart.');
      this.router.navigate(['/cart']);
    }
  }

  proceedToPay(form: NgForm): void {
    if (form.invalid) {
      form.form.markAllAsTouched();
      return;
    }

    if (!this.currentUser) {
      alert('Please login to continue.');
      this.router.navigate(['/login']);
      return;
    }

    const customerDetails = {
      userId: this.currentUser.id,
      name: this.customerName,
      email: this.currentUser.email,
      phone: this.contactInfo,
      contact: this.contactInfo,
      address: this.shippingAddress
    };

    const paymentOrder = {
      items: this.orderDetails,
      totalAmount: this.totalAmount,
      customerDetails: customerDetails
    };

    this.orderService.setPaymentOrder(paymentOrder);
    this.isFormSubmitted = true;
    this.router.navigate(['/payment']);
  }

  canDeactivate(): boolean {
    const hasUnsavedChanges = !this.isFormSubmitted &&
      (this.customerName || this.contactInfo || this.shippingAddress);
    return hasUnsavedChanges
      ? confirm('You have unsaved changes. Are you sure you want to leave this page?')
      : true;
  }
}