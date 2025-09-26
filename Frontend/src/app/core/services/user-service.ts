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
          if (response && response.id) {
            // Backend returns UserResponseDto directly
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

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/all`);
  }

  getToken(): string | null {
    const user = this.getCurrentUser();
    return user?.token || null;
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${userId}`);
  }

  updateUser(userId: number, updatedUser: User): Observable<any> {
    return this.http.put(`${this.apiUrl}/${userId}`, updatedUser);
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${userId}`);
  }

  refreshCurrentUserFromBackend(userId: number): void {
    this.http.get<User>(`${this.apiUrl}/${userId}`).subscribe({
      next: (user) => {
        this.currentUserSubject.next(user);
        localStorage.setItem('currentUser', JSON.stringify(user));
      }
    });
  }

  private loadUserFromStorage(): User | null {
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }
}
