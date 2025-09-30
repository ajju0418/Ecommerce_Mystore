import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = environment.apiUrl + '/users';

  private currentUserSubject = new BehaviorSubject<User | null>(this.loadUserFromStorage());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    const loginData = { username, password };
    return new Observable(observer => {
      this.http.post(`${this.apiUrl}/login`, loginData).subscribe({
        next: (response: any) => {
          if (response && response.id && response.token) {
            this.currentUserSubject.next(response);
            localStorage.setItem('currentUser', JSON.stringify(response));
            observer.next({ success: true, user: response });
          } else {
            observer.next({ success: false, message: 'Invalid credentials' });
          }
          observer.complete();
        },
        error: (error) => {
          observer.next({ success: false, message: 'Login failed' });
          observer.complete();
        }
      });
    });
  }

  logout(): void {
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  updateCurrentUser(updatedUser: User): Observable<any> {
    return new Observable(observer => {
      this.http.put(`${this.apiUrl}/profile`, updatedUser).subscribe({
        next: (response: any) => {
          this.currentUserSubject.next(updatedUser);
          localStorage.setItem('currentUser', JSON.stringify(updatedUser));
          observer.next({ success: true, user: updatedUser });
          observer.complete();
        },
        error: (error) => {
          observer.next({ success: false, message: 'Update failed' });
          observer.complete();
        }
      });
    });
  }

  register(user: User): Observable<any> {
    return new Observable(observer => {
      this.http.post(`${this.apiUrl}/register`, user).subscribe({
        next: (response: any) => {
          // Backend returns UserResponseDto directly
          observer.next({ success: true, message: 'Registration successful', user: response });
          observer.complete();
        },
        error: (error) => {
          observer.next({ success: false, message: 'Registration failed' });
          observer.complete();
        }
      });
    });
  }

  resetPassword(username: string, phone: string, newPassword: string): Observable<any> {
    const body = { username, phone, newPassword } as any;
    return new Observable(observer => {
      this.http.post(`${this.apiUrl}/reset-password`, body).subscribe({
        next: (response: any) => {
          observer.next(response); // Pass backend response directly
          observer.complete();
        },
        error: (error) => {
          observer.next({ success: false, message: 'Reset failed' });
          observer.complete();
        }
      });
    });
  }

  getAllUsers(): Observable<User[]> {
    const token = this.getToken();
    const headers = token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
    return this.http.get<User[]>(`${this.apiUrl}/all`, { headers });
  }

  getToken(): string | null {
    // Try admin first, then fallback to user
    const admin = localStorage.getItem('currentAdmin');
    if (admin) {
      try {
        const adminObj = JSON.parse(admin);
        if (adminObj?.token) return adminObj.token;
      } catch {}
    }
    const user = localStorage.getItem('currentUser');
    if (user) {
      try {
        const userObj = JSON.parse(user);
        if (userObj?.token) return userObj.token;
      } catch {}
    }
    return null;
  }

  isTokenValid(): boolean {
    const token = this.getToken();
    if (!token) return false;
    
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Math.floor(Date.now() / 1000);
      return payload.exp > currentTime;
    } catch (error) {
      return false;
    }
  }

  deleteUser(userId: number): Observable<any> {
    const token = this.getToken();
    const headers = token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
    return this.http.delete(`${this.apiUrl}/${userId}`, { headers, responseType: 'text' });
  }

  updateUser(userId: number, updatedUser: User): Observable<any> {
    return this.http.put(`${this.apiUrl}/${userId}`, updatedUser);
  }

  getUserById(userId: number): Observable<User> {
    const token = this.getToken();
    const headers = token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
    return this.http.get<User>(`${this.apiUrl}/${userId}`, { headers });
  }

  refreshCurrentUserFromBackend(userId: number): void {
    const token = this.getToken() || '';
    const headers = token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
    this.http.get<User>(`${this.apiUrl}/${userId}`, { headers }).subscribe({
      next: (user) => {
        // Ensure token is a string, not null
        const userWithToken = { ...user, token };
        this.currentUserSubject.next(userWithToken);
        localStorage.setItem('currentUser', JSON.stringify(userWithToken));
      }
    });
  }

  private loadUserFromStorage(): User | null {
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }
}
