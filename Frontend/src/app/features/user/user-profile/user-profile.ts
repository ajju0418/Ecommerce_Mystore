import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../core/services/user-service';
import { User } from '../../../core/models/user.model';
import { Header } from '../../../layout/header/header';
import { Footer } from '../../../layout/footer/footer';

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

  constructor(private userService: UserService) {}

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
          this.user = { ...this.editForm };
          this.editMode = false;
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
}