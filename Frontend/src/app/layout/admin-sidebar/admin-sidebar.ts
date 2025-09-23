import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { OrderService, Order } from '../../core/services/order-service';

@Component({
  selector: 'app-admin-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-sidebar.html',
  styleUrl: './admin-sidebar.css'
})
export class AdminSidebar implements OnInit {
  @Output() sectionChange = new EventEmitter<string>();
  pendingOrdersCount = 0;

  constructor(public router: Router, private orderService: OrderService) {}

  menuItems = [
    { id: 'dashboard', label: 'Dashboard', icon: 'dashboard', route: '/admin/dashboard' },
    { id: 'products', label: 'Product List', icon: 'products', route: '/admin/products' },
    { id: 'sales', label: 'Sales', icon: 'sales', route: '/admin/sales', showBadge: true },
    { id: 'customers', label: 'Customers', icon: 'customers', route: '/admin/customers' },

    { id: 'logout', label: 'Log out', icon: 'logout', route: '/adminlogin' }
  ];

  ngOnInit() {
    this.updatePendingOrdersCount();
  }

  updatePendingOrdersCount() {
    this.orderService.getOrders().subscribe((orders: Order[]) => {
      this.pendingOrdersCount = orders.filter((order: Order) => order.status === 'pending').length;
    });
  }

  isActive(route: string): boolean {
    return this.router.url.includes(route);
  }

  onMenuItemClick(section: string) {
    if (section === 'logout') {
      this.logout();
    } else {
      this.sectionChange.emit(section);
      const selectedItem = this.menuItems.find(item => item.id === section);
      if (selectedItem) {
        this.router.navigate([selectedItem.route]);
      }
    }
  }

  logout() {
    localStorage.removeItem('isAdmin');
    this.router.navigate(['/adminlogin'], { replaceUrl: true });
  }
}
