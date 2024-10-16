import { ChangeDetectionStrategy,  Component, inject, Input, model, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {  Tile, TileContainerComponent } from "../../library/tile-container/tile-container.component";
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA,MatDialogRef, MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatListModule } from '@angular/material/list';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ActivatedRoute } from '@angular/router';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-configure',
  standalone: true,
  imports: [CommonModule, TileContainerComponent, ReactiveFormsModule,
    MatTableModule
    ],
  templateUrl: './configure.component.html',
  styleUrls: ['./configure.component.scss'],
})
export class ConfigureComponent {
  @Input('subsystem') subSystem: string = '';
  // tiles: Tile[] = [
  //   { label: 'Requests', action: ()=>{} },
  //   { label: 'Responses', action: ()=>{} }
  // ]; 

  constructor(private dialog: MatDialog) {    
  }
}


