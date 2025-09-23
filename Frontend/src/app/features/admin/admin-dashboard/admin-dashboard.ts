import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { AdminSidebar } from '../../../layout/admin-sidebar/admin-sidebar';
import { filter } from 'rxjs/operators';

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

  constructor(private router: Router) {}

  ngOnInit() {
    // ✅ Redirect if admin session is missing
    const isAdmin = localStorage.getItem('isAdmin');
    if (isAdmin !== 'true') {
      this.router.navigate(['/adminlogin'], { replaceUrl: true });
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
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('adminRole');
    localStorage.removeItem('adminName');
    this.router.navigate(['/home'], { replaceUrl: true });
  }
}
