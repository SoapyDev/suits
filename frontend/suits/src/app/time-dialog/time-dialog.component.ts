import {ChangeDetectionStrategy, Component, inject, model} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
} from '@angular/material/dialog';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {FormsModule} from '@angular/forms';
import {TimeData} from '../task/components/task-detail/task-detail.component';

@Component({
  selector: 'app-time-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatFormField,
    MatInput,
    MatButtonModule,
    MatLabel,
    FormsModule,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
  ],
  templateUrl: './time-dialog.component.html',
  styleUrl: './time-dialog.component.scss'
})
export class TimeDialogComponent {

  readonly dialogRef = inject(MatDialogRef<TimeDialogComponent>);
  readonly data = inject<TimeData>(MAT_DIALOG_DATA);
  readonly value = model(this.data.value);
  readonly duration = model(this.data.duration);

  onNoClick(): void{
    this.dialogRef.close();
  }

  save(): void {
    this.dialogRef.close({ value: this.value(), duration: this.duration() });
  }
}
