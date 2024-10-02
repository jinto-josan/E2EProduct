import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Tile, TileContainerComponent } from "../../library/tile-container/tile-container.component";

@Component({
  selector: 'app-configure',
  standalone: true,
  imports: [CommonModule, TileContainerComponent],
  templateUrl: './configure.component.html',
  styleUrl: './configure.component.scss',
})
export class ConfigureComponent {

  tiles :Tile[] = [
    { label: 'SubSystems', link: '/configure' },
    { label: 'Requests', link: '/testing' },
    { label: 'Responses', link: '/testing' }
  ]
}
