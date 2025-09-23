import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';

interface User {
  username: string;
  email: string;
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class Header implements OnInit, OnDestroy {
  open = false;
  currentUser: User | null = null;
  cartCount = 0;
  showUserDropdown = false;
  private userSub!: Subscription;
  private cartSub!: Subscription;

  constructor(public router: Router) {}

  ngOnInit(): void {
    // TODO: Replace with actual backend service calls
    // this.userService.currentUser$.subscribe(user => this.currentUser = user);
    // this.cartService.cartItems$.subscribe(items => this.cartCount = items.length);
    
    // Load user from localStorage for now
    this.loadCurrentUser();
    this.cartCount = 3; // Mock cart count
  }

  loadCurrentUser(): void {
    const userData = localStorage.getItem('currentUser');
    if (userData) {
      this.currentUser = JSON.parse(userData);
    }
  }

  ngOnDestroy(): void {
    // TODO: Unsubscribe from actual services when implemented
    // if (this.userSub) this.userSub.unsubscribe();
    // if (this.cartSub) this.cartSub.unsubscribe();
  }

  toggleUserDropdown() {
    this.showUserDropdown = !this.showUserDropdown;
  }

  hideUserDropdown() {
    this.showUserDropdown = false;
  }

  logout(): void {
    // TODO: Replace with actual backend logout
    // this.userService.logout();
    
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('currentUser');
    this.currentUser = null;
    this.router.navigate(['/home']);
    this.hideUserDropdown();
  }
}
