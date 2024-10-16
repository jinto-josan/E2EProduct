import { ChangeDetectionStrategy, Component, inject, model, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {  Tile, TileContainerComponent } from '../../library/tile-container/tile-container.component';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { SubSystemFormComponent } from '../../forms/sub-system-form/sub-system-form.component';
import { Router } from '@angular/router';
import { ConfigurationControllerService, SubsystemProjection } from '../../api-client';

@Component({
  selector: 'app-pages-dashboard',
  standalone: true,
  imports: [CommonModule, TileContainerComponent,],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  subSystem=signal('');
  

  tiles:Tile[] = [
    { label: 'Configure', action:()=>
      {
        const dialogRef=this.dialog.open(SubSystemFormComponent,
        );
        dialogRef.afterClosed().subscribe(result => {
          if(result){
            this.subSystem.set(result);
            this.router.navigate(['/configure',result]);
          }
        });
      }
    },
    { label: 'Run Tests', link: '/testing' },
  ];
  constructor(private dialog: MatDialog, private router:Router) {    
  }

 
}



