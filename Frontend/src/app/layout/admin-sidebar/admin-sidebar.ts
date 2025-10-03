import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { OrderService, Order } from '../../core/services/order-service';
import { UserService } from '../../core/services/user-service';

interface MenuItem {
  id: string;
  label: string;
  icon: string;
  route: string;
  badge?: string;
}

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
  userRole: string | null = null;

  constructor(
    public router: Router,
    private orderService: OrderService,
    private userService: UserService
  ) {}

  menuItems: MenuItem[] = [
    { id: 'dashboard', label: 'Dashboard', icon: 'home', route: '/admin/dashboard' },
    { id: 'sales', label: 'sales', icon: 'sales', route: '/admin/sales', badge: '0' },
    { id: 'products', label: 'Products', icon: 'products', route: '/admin/products' },
    { id: 'customers', label: 'Customers', icon: 'reports', route: '/admin/customers' },
    { id: 'admin-list', label: 'Admin List', icon: 'admin-list', route: '/admin/admin-list' },
    { id: 'logout', label: 'Logout', icon: 'logout', route: '/home' }
  ];

  ngOnInit() {
    this.updatePendingOrdersCount();
    this.userRole = this.getUserRole();
  }

  updatePendingOrdersCount() {
    this.orderService.getAllOrders().subscribe((orders: Order[]) => {
      this.pendingOrdersCount = orders.filter((order: Order) => order.status === 'pending').length;
      
      // Update the badge for sales menu item
      const salesItem = this.menuItems.find(item => item.id === 'sales');
      if (salesItem) {
        salesItem.badge = this.pendingOrdersCount > 0 ? this.pendingOrdersCount.toString() : undefined;
      }
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

  logout(): void {
    this.userService.logout();
    localStorage.removeItem('isAdmin');
    this.router.navigate(['/home'], { replaceUrl: true });
  }

  getUserRole(): string | null {
    const isAdmin = localStorage.getItem('isAdmin') === 'true';
    return isAdmin ? 'admin' : 'user';
  }
}
