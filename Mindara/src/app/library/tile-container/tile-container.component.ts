import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
export type Tile = { label: string, link: string };

@Component({
  selector: 'app-tile-container',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './tile-container.component.html',
  styleUrl: './tile-container.component.scss',
})
export class TileContainerComponent {
  @Input() tileConfig: Tile[] = [];


}
