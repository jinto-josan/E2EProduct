import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Tile, TileContainerComponent } from '../../library/tile-container/tile-container.component';

@Component({
  selector: 'app-pages-dashboard',
  standalone: true,
  imports: [CommonModule, TileContainerComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  tiles:Tile[] = [
    { label: 'Configure', link: '/configure' },
    { label: 'Run Tests', link: '/testing' },
  ]
}
