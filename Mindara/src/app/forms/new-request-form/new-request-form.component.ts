import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import {  Request } from '../../api-client';

@Component({
  selector: 'app-new-request-form',
  templateUrl: './new-request-form.component.html',
  styleUrls: ['./new-request-form.component.scss'],
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule,
    ReactiveFormsModule,MatListModule,CommonModule,MatAutocompleteModule],
})
export class NewRequestFormComponent implements OnInit {
  requestForm: any;
  methods= Object.values(Request.MethodEnum);

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<NewRequestFormComponent>
  ) {
  }

  ngOnInit(): void {
    this.requestForm = this.fb.group({
      path: ['', Validators.required],
      method: ['', Validators.required],
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.requestForm.valid) {
      this.dialogRef.close([this.requestForm.value as Request]);
    }
  }
}