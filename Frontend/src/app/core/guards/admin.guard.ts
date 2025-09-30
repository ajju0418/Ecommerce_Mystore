import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../services/user-service';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private router: Router, private userService: UserService) {}

  canActivate(): boolean {
    const user = this.userService.getCurrentUser();
    const token = this.userService.getToken();
    
    if (!user || !token || !this.userService.isTokenValid()) {
      this.userService.logout();
      this.router.navigate(['/login']);
      return false;
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const role = payload['role'];
      if (role === 'ADMIN') {
        return true;
      }
    } catch (error) {
      console.error('Error parsing JWT token:', error);
    }

    // Not admin - redirect to home instead of login
    this.router.navigate(['/home']);
    return false;
  }
}
