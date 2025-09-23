import { Component } from '@angular/core';
import { Header } from '../../layout/header/header';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-found',
  imports: [Header, RouterLink],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css'
})
export class NotFound {

}
