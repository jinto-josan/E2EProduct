import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule} from '@angular/material/button'
export type Tile = { label: string, link: string };

@Component({
  selector: 'app-tile-container',
  standalone: true,
  imports: [CommonModule,RouterModule,MatButtonModule],
  templateUrl: './tile-container.component.html',
  styleUrl: './tile-container.component.scss',
})
export class TileContainerComponent {
  @Input() tileConfig: Tile[] = [];


}
