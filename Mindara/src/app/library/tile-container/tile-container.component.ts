import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule} from '@angular/material/button'
export type Tile = { label: string, action?: () => void, link?: string };
// export type LinkTile= Tile & { link: string };
// export type ActionTile = Tile & { action: () => void };

@Component({
  selector: 'app-tile-container',
  standalone: true,
  imports: [CommonModule,RouterModule,MatButtonModule],
  templateUrl: './tile-container.component.html',
  styleUrl: './tile-container.component.scss',
})
export class TileContainerComponent {
  @Input() tileConfig: Tile[] = [];

  constructor(private route: Router) { }

  handleTileClick(tile: Tile) {
    if('action' in tile && 'link' in tile)
    {
      throw new Error('Tile cannot have both action and link');
    }
    if('action' in tile)
      this.handleActionTile(tile );
    if('link' in tile)
      this.handleLinkTile(tile );
  }
  


  handleLinkTile( tile:Tile) {
    this.route.navigate([tile.link]);
  }
  handleActionTile(tile: Tile) {
    if (tile.action) {
      tile.action();
    }
  }

  


}
