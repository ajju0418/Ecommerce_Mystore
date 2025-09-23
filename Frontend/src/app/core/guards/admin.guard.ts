import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    // TODO: Replace with actual backend authentication check
    // const token = localStorage.getItem('adminToken');
    // return this.authService.validateAdminToken(token);
    
    const isAdmin = localStorage.getItem('isAdmin') === 'true';
    console.log('AdminGuard: isAdmin =', isAdmin);
    console.log('AdminGuard: localStorage isAdmin =', localStorage.getItem('isAdmin'));
    
    if (isAdmin) {
      console.log('AdminGuard: Access granted');
      return true;
    }

    console.log('AdminGuard: Access denied, redirecting to adminlogin');
    this.router.navigate(['/adminlogin']);
    return false;
  }
}
