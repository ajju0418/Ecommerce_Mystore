import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../../core/services/user-service';
import { Header } from "../../../layout/header/header";

@Component({
  selector: 'app-user-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, Header],
  templateUrl: './user-login.html',
  styleUrls: ['./user-login.css']
})
export class UserLogin {
  loginForm: FormGroup;
  loginError = false;
  errorMessage = '';
  showPassword = false;
  forgotMode = false;
  forgotForm: FormGroup;
  resetError = '';
  resetSuccess = '';
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.pattern(/^[A-Za-z][A-Za-z0-9]*$/)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(6)
      ]]
    });

    this.forgotForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    this.loginError = false;
    this.errorMessage = '';

    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;
      
      this.userService.login(username, password).subscribe({
        next: (response) => {
          if (response.success) {
            const user = response.user || null;
            const token: string | undefined = user?.token;
            if (token) {
              try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                if (payload['role'] === 'ADMIN') {
                  this.router.navigate(['/admin']);
                  return;
                }
              } catch {}
            }
            this.router.navigate(['/home']);
          } else {
            this.loginError = true;
            this.errorMessage = response.message || 'Login failed';
          }
        },
        error: (error) => {
          this.loginError = true;
          this.errorMessage = 'Login failed. Please try again.';
        }
      });
    } else {
      this.markFormGroupTouched(this.loginForm);
    }
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach((control: any) => {
      control.markAsTouched();
      if (control.controls) {
        this.markFormGroupTouched(control);
      }
    });
  }
  togglePasswordVisibility(): void {
  this.showPassword = !this.showPassword;
}

  toggleForgot(): void {
    this.forgotMode = !this.forgotMode;
    this.loginError = false;
    this.errorMessage = '';
  }

  sendReset(): void {
    this.resetError = '';
    this.resetSuccess = '';
    if (this.forgotForm.invalid) {
      this.markFormGroupTouched(this.forgotForm);
      return;
    }
    const { username, phone, newPassword } = this.forgotForm.value;
    this.userService.resetPassword(username, phone, newPassword).subscribe(res => {
      if (res.success) {
        this.resetSuccess = res.message || 'Password updated successfully. Please sign in with your new password.';
        setTimeout(() => {
          this.forgotMode = false;
          this.forgotForm.reset();
          this.resetSuccess = '';
        }, 2000);
      } else {
        this.resetError = res.message || 'Reset failed';
      }
    });
  }
}
