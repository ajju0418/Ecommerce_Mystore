import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../core/services/user-service';
import { User } from '../../../core/models/user.model';
import { Header } from '../../../layout/header/header';
import { Footer } from '../../../layout/footer/footer';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, Header, Footer],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css'
})
export class UserProfile implements OnInit {
  user: User | null = null;
  editMode = false;
  editForm: User = {} as User;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {
    this.user = this.userService.getCurrentUser();
    if (this.user && this.user.id) {
      this.userService.getUserById(this.user.id).subscribe({
        next: (user) => {
          this.user = user;
          this.editForm = { ...user };
        }
      });
    }
  }

  toggleEditMode() {
    this.editMode = !this.editMode;
    if (this.user) {
      this.editForm = { ...this.user };
    }
  }

  saveProfile() {
    if (this.editForm && this.editForm.id) {
      this.userService.updateUser(this.editForm.id, this.editForm).subscribe({
        next: (response) => {
          // Fetch updated user from backend to ensure UI reflects DB
          this.userService.getUserById(this.editForm.id!).subscribe({
            next: (user) => {
              this.user = user;
              this.editMode = false;
            }
          });
        },
        error: () => {
          // Optionally show error message
        }
      });
    }
  }

  cancelEdit() {
    this.editMode = false;
    if (this.user) {
      this.editForm = { ...this.user };
    }
  }

  deleteAccount() {
    if (this.user && this.user.id) {
      if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
        this.userService.deleteUser(this.user.id).subscribe({
          next: () => {
            this.userService.logout();
            this.router.navigate(['/login']);
          },
          error: () => {
            alert('Failed to delete account. Please try again.');
          }
        });
      }
    }
  }
}