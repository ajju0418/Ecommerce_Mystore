import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Body } from "../../shared/body/body";
import { Header } from '../../layout/header/header';
import { Footer } from '../../layout/footer/footer';


@Component({
  selector: 'app-home',
  imports: [ Body,Header,Footer,RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

}
