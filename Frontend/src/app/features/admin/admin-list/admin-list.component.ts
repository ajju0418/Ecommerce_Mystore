import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface AdminUser {
  id: number;
  username: string;
  email: string;
  roles: string[];
  status: 'Active' | 'Inactive';
  createdAt: Date;
}

@Component({
  selector: 'app-admin-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-list.component.html',
  styleUrls: ['./admin-list.component.css']
})
export class AdminListComponent implements OnInit {
  admins: AdminUser[] = [];
  showAddForm = false;
  editingAdmin: AdminUser | null = null;
  
  newAdmin = {
    username: '',
    email: '',
    password: '',
    roles: [] as string[]
  };

  availableRoles = [
    'create products',
    'update products', 
    'view products',
    'delete products',
    'accept orders',
    'view orders',
    'view customer'
  ];

  constructor() {}

  ngOnInit(): void {
    this.loadAdmins();
  }

  loadAdmins(): void {
    // TODO: Replace with actual backend service call
    // this.adminService.getAdmins().subscribe(admins => this.admins = admins);
    
    // Mock data for now
    this.admins = [
      {
        id: 1,
        username: 'admin',
        email: 'admin@store.com',
        roles: ['create products', 'update products', 'view products', 'delete products', 'accept orders', 'view orders', 'view customer'],
        status: 'Active',
        createdAt: new Date()
      },
      {
        id: 2,
        username: 'manager',
        email: 'manager@store.com',
        roles: ['view products', 'accept orders', 'view orders', 'view customer'],
        status: 'Active',
        createdAt: new Date()
      }
    ];
  }

  addAdmin(): void {
    if (this.newAdmin.username && this.newAdmin.email && this.newAdmin.password) {
      // TODO: Replace with actual backend service call
      // this.adminService.createAdmin(this.newAdmin).subscribe(() => {
      //   this.loadAdmins();
      //   this.resetForm();
      // });
      
      const admin: AdminUser = {
        id: Date.now(),
        username: this.newAdmin.username,
        email: this.newAdmin.email,
        roles: [...this.newAdmin.roles],
        status: 'Active',
        createdAt: new Date()
      };
      
      this.admins.push(admin);
      this.resetForm();
      this.showAddForm = false;
    }
  }

  editAdmin(admin: AdminUser): void {
    this.editingAdmin = { ...admin };
  }

  updateAdmin(): void {
    if (this.editingAdmin) {
      // TODO: Replace with actual backend service call
      // this.adminService.updateAdmin(this.editingAdmin).subscribe(() => {
      //   this.loadAdmins();
      //   this.editingAdmin = null;
      // });
      
      const index = this.admins.findIndex(a => a.id === this.editingAdmin!.id);
      if (index !== -1) {
        this.admins[index] = { ...this.editingAdmin };
      }
      this.editingAdmin = null;
    }
  }

  deleteAdmin(id: number): void {
    if (confirm('Are you sure you want to delete this admin?')) {
      // TODO: Replace with actual backend service call
      // this.adminService.deleteAdmin(id).subscribe(() => this.loadAdmins());
      
      this.admins = this.admins.filter(admin => admin.id !== id);
    }
  }

  toggleRole(role: string): void {
    const index = this.newAdmin.roles.indexOf(role);
    if (index > -1) {
      this.newAdmin.roles.splice(index, 1);
    } else {
      this.newAdmin.roles.push(role);
    }
  }

  toggleEditRole(role: string): void {
    if (this.editingAdmin) {
      const index = this.editingAdmin.roles.indexOf(role);
      if (index > -1) {
        this.editingAdmin.roles.splice(index, 1);
      } else {
        this.editingAdmin.roles.push(role);
      }
    }
  }

  resetForm(): void {
    this.newAdmin = {
      username: '',
      email: '',
      password: '',
      roles: []
    };
  }

  cancelEdit(): void {
    this.editingAdmin = null;
  }
}