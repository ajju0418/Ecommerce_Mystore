import { Component } from '@angular/core';
import { Footer } from "../../layout/footer/footer";
import { Header } from "../../layout/header/header";
import { CanComponentDeactivate } from '../../core/guards/unsaved-changes.guard'; // Adjust path as needed
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact-us',
  standalone: true,
  imports: [Footer, Header,FormsModule],
  templateUrl: './contact-us.html',
  styleUrl: './contact-us.css'
})
export class ContactUs implements CanComponentDeactivate {
  name: string = '';
  email: string = '';
  message: string = '';
  isSubmitted: boolean = false;

  onSubmit() {
    alert('Your message has been sent successfully!');
    this.isSubmitted = true;
    this.name = '';
    this.email = '';
    this.message = '';
  }

  canDeactivate(): boolean {
    const hasUnsavedChanges = !this.isSubmitted && (this.name || this.email || this.message);
    return hasUnsavedChanges
      ? confirm('You have unsent message. Are you sure you want to leave this page?')
      : true;
  }
}
