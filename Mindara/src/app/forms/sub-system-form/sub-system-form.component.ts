import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { ConfigurationControllerService, SubsystemProjection } from '../../api-client';

@Component({
  selector: 'app-sub-system-form',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule,
    ReactiveFormsModule,MatListModule,CommonModule,MatAutocompleteModule],
  templateUrl: './sub-system-form.component.html',
  styleUrl: './sub-system-form.component.scss',
  providers: [{provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {floatLabel: 'always'}}],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SubSystemFormComponent {
  subsystemForm: any;
  readonly dialogRef = inject(MatDialogRef<SubSystemFormComponent>);
  // readonly subsystemList=inject(MAT_DIALOG_DATA)
  subsystemList= toSignal(inject(ConfigurationControllerService).getSubSystems(), { initialValue: [] as SubsystemProjection[] });
  subSystemSelected=signal('');
  isNewSubsystem=computed(()=>{
    return this.subSystemSelected()  && !this.subsystemList()?.some((subsystem: SubsystemProjection) => subsystem.name === this.subSystemSelected())
});
  constructor(private fb: FormBuilder) {
    this.subsystemForm = this.fb.group({
        subsystem: ['',Validators.required],
        description: ['']
    });
  }
  ngOnInit(){
    
    this.subsystemForm.valueChanges.subscribe((value: { subsystem: string;})=>{
      this.subSystemSelected.set(value.subsystem);
    })
  }
  handleClick(){
    //Has to handle form closing here because validation has to be checked before closing
    const {value,valid}=this.subsystemForm
    if(valid){
      this.dialogRef.close(this.subSystemSelected());
    }
    
  } 
}
