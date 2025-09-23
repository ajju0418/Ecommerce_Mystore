import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface ErrorMessage {
  message: string;
  type: 'error' | 'warning' | 'info' | 'success';
  duration?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  private errorSubject = new BehaviorSubject<ErrorMessage | null>(null);
  public error$ = this.errorSubject.asObservable();

  showError(message: string, duration: number = 5000): void {
    this.errorSubject.next({ message, type: 'error', duration });
    this.clearAfterDelay(duration);
  }

  showSuccess(message: string, duration: number = 3000): void {
    this.errorSubject.next({ message, type: 'success', duration });
    this.clearAfterDelay(duration);
  }

  showWarning(message: string, duration: number = 4000): void {
    this.errorSubject.next({ message, type: 'warning', duration });
    this.clearAfterDelay(duration);
  }

  showInfo(message: string, duration: number = 3000): void {
    this.errorSubject.next({ message, type: 'info', duration });
    this.clearAfterDelay(duration);
  }

  clear(): void {
    this.errorSubject.next(null);
  }

  private clearAfterDelay(duration: number): void {
    setTimeout(() => this.clear(), duration);
  }
}