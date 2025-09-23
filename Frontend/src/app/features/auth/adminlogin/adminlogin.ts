import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './adminlogin.html',
  styleUrl: './adminlogin.css'
})
export class Adminlogin {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {
    this.loginForm = this.fb.group({
      adminId: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

onSubmit() {
  if (this.loginForm.valid) {
    const { adminId, password } = this.loginForm.value;
    
    // TODO: Replace with actual backend service call
    // this.userService.adminLogin(adminId, password).subscribe(...);
    
    // Hardcoded admin credentials for now
    if (adminId === 'admin' && password === 'admin123') {
      console.log('Login successful, setting localStorage');
      localStorage.setItem('isAdmin', 'true');
      localStorage.setItem('adminRole', 'ADMIN');
      localStorage.setItem('adminName', 'admin');
      console.log('Navigating to admin dashboard');
      
      // Use setTimeout to ensure localStorage is set before navigation
      setTimeout(() => {
        this.router.navigate(['/admin']).then(success => {
          console.log('Navigation result:', success);
          if (!success) {
            console.error('Navigation failed');
          }
        }).catch(error => {
          console.error('Navigation error:', error);
        });
      }, 100);
    } else {
      alert('Invalid credentials. Use admin/admin123');
    }
  } else {
    this.markFormGroupTouched();
  }
}


  private markFormGroupTouched() {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }
}
