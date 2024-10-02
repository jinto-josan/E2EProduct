import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {  HeaderComponent } from './header/header.component';
import {  DashboardComponent } from "./pages/dashboard/dashboard.component";

@Component({
  standalone: true,
  imports: [RouterModule, DashboardComponent, HeaderComponent],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'E2E-UI';
}
