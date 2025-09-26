import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../core/services/user-service';
import { CartService } from '../../core/services/cart-Service';

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

  constructor(public router: Router, private userService: UserService, private cartService: CartService) {}

  ngOnInit(): void {
    this.userSub = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user && user.id) {
        this.userService.refreshCurrentUserFromBackend(user.id);
        this.cartService.loadUserCart(user.id);
        this.cartSub = this.cartService.cartCount$.subscribe(count => {
          this.cartCount = count;
        });
      }
    });
  }

  ngOnDestroy(): void {
    if (this.userSub) this.userSub.unsubscribe();
    if (this.cartSub) this.cartSub.unsubscribe();
  }

  toggleUserDropdown() {
    this.showUserDropdown = !this.showUserDropdown;
  }

  hideUserDropdown() {
    this.showUserDropdown = false;
  }

  logout(): void {
    this.userService.logout();
    this.currentUser = null;
    this.router.navigate(['/home']);
    this.hideUserDropdown();
  }
}
