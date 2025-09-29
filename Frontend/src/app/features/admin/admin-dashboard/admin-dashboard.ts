import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { AdminSidebar } from '../../../layout/admin-sidebar/admin-sidebar';
import { filter } from 'rxjs/operators';
import { UserService } from '../../../core/services/user-service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    AdminSidebar
  ],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboard implements OnInit {
  activeSection = 'dashboard';

  constructor(private router: Router, private userService: UserService) {}

  ngOnInit() {
    const token = this.userService.getToken();
    if (!token) {
      this.router.navigate(['/login'], { replaceUrl: true });
      return;
    }
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (payload['role'] !== 'ADMIN') {
        this.router.navigate(['/login'], { replaceUrl: true });
        return;
      }
    } catch {
      this.router.navigate(['/login'], { replaceUrl: true });
      return;
    }

    // ✅ Track active section based on route
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        const segments = (event as NavigationEnd).urlAfterRedirects.split('/');
        this.activeSection = segments[segments.length - 1] || 'dashboard';
      });
  }

  setActiveSection(section: string) {
    this.activeSection = section;
  }

  goBackToStore() {
    this.userService.logout();
    this.router.navigate(['/home'], { replaceUrl: true });
  }
}
