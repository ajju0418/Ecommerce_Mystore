import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { UserService } from '../services/user-service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private userService: UserService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.userService.getToken();
    console.log('Auth Interceptor - URL:', req.url, 'Token:', token ? 'Present' : 'Missing');
    
    if (token) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      console.log('Auth Interceptor - Adding Authorization header');
      return next.handle(authReq);
    }
    
    console.log('Auth Interceptor - No token, sending request without auth');
    return next.handle(req);
  }
}