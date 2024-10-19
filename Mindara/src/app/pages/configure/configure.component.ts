import { CommonModule } from '@angular/common';
import {
  Component,
  inject,
  Input
} from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule } from '@angular/material/paginator';
import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { ConfigurationControllerService } from '../../api-client';
import { MiniTableComponent } from '../../library/mini-table/mini-table.component';
import {
  TileContainerComponent
} from '../../library/tile-container/tile-container.component';

@Component({
  selector: 'app-configure',
  standalone: true,
  imports: [
    CommonModule,
    TileContainerComponent,
    ReactiveFormsModule,
    MatTableModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MiniTableComponent,
  ],
  templateUrl: './configure.component.html',
  styleUrls: ['./configure.component.scss'],
})
export class ConfigureComponent {
  @Input('subsystem') subSystem: string = '';

  private configService = inject(ConfigurationControllerService);
  dataSourceFn = (pageIndex: number, pageSize: number) =>
    this.configService!.getRequests(this.subSystem, pageIndex, pageSize);
}

// data!: RequestId[] ;

// resultsLength=0;
// isLoadingResults = true;
// isRateLimitReached = false;

// @ViewChild(MatPaginator) paginator!: MatPaginator;
// @ViewChild(MatSort) sort!: MatSort ;

// ngAfterViewInit() {

//   // If the user changes the sort order, reset back to the first page.
//   // this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

//   // merge(this.sort.sortChange, this.paginator.page)
//   merge( this.paginator.page)
//     .pipe(
//       startWith({}),
//       switchMap(() => {
//         this.isLoadingResults = true;
//         return this.configService!.getRequests(this.subSystem,
//           this.paginator.pageIndex,this.paginator.pageSize
//         ).pipe(catchError(() => of(null)));
//       }),
//       map(data => {
//         // Flip flag to show that loading has finished.
//         this.isLoadingResults = false;
//         this.isRateLimitReached = data === null;

//         if (data === null) {
//           return [];
//         }

//         // Only refresh the result length if there is new data. In case of rate
//         // limit errors, we do not want to reset the paginator to zero, as that
//         // would prevent users from re-triggering requests.
//         this.resultsLength = data.totalElements??0;
//         return data.content;
//       }),
//     )
//     .subscribe(data => (this.data = data??[]));
// }
// newRequest(){
//   const dialogRef=this.dialog.open(NewRequestFormComponent,
//     {data: {subSystem: this.subSystem}}
//   );
//   dialogRef.afterClosed().subscribe(result  => {
//       if(result){
//         this.isLoading.set(true);
//         this.configService.createRequest(this.subSystem, result,"response").subscribe(
//           {next: (response)=>{
//             if(response.ok)
//               console.log("Created request")},
//             error: (error)=>{this.isLoading.set(false);console.log(error)},
//             complete:()=>{ this.isLoading.set(false)}}
//         )

//       }
//   });
// }
