import { Component } from '@angular/core';
import { Header } from "../../layout/header/header";
import { Footer } from "../../layout/footer/footer";
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'app-about-us',
  imports: [Header, Footer],
  templateUrl: './about-us.html',
  styleUrl: './about-us.css'
})
export class AboutUs {
     
}
