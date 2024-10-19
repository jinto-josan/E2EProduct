import { HttpClient } from '@angular/common/http';
import { Component, inject, input, Input, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { merge, startWith, switchMap, catchError, of, map, Observable } from 'rxjs';
import { ConfigurationControllerService, RequestId } from '../../api-client';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { TileContainerComponent } from '../tile-container/tile-container.component';

@Component({
  selector: 'app-mini-table',
  standalone: true,
  imports: [CommonModule, TileContainerComponent, ReactiveFormsModule,
    MatTableModule,MatButtonModule,MatProgressSpinnerModule,MatPaginatorModule],
  templateUrl: './mini-table.component.html',
  styleUrl: './mini-table.component.scss'
})
export class MiniTableComponent {
  columns=input.required<string[]>();
  pageSize= input(10);
  dataSourceFn = input.required<(pageIndex:number,pageSize:number)=>Observable<any>>();

  data!: any[] ;

  resultsLength=0;
  isLoadingResults = true;
  isRateLimitReached = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort ;


  ngAfterViewInit() {

    // If the user changes the sort order, reset back to the first page.
    // this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    // merge(this.sort.sortChange, this.paginator.page)
    merge( this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.dataSourceFn()(this.paginator.pageIndex,this.paginator.pageSize).pipe(catchError(() => of(null)));
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.isRateLimitReached = data === null;

          if (data === null) {
            return [];
          }

          // Only refresh the result length if there is new data. In case of rate
          // limit errors, we do not want to reset the paginator to zero, as that
          // would prevent users from re-triggering requests.
          this.resultsLength = data.totalElements??0;
          return data.content;
        }),
      )
      .subscribe(data => (this.data = data??[]));
  }
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
}
