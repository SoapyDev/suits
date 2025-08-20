import {Component, inject, model} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
} from '@angular/material/dialog';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatButtonModule} from '@angular/material/button';
import {MatInput} from '@angular/material/input';
import {NoteData} from '../task/components/task-detail/task-detail.component';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-note-dialog',
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
  templateUrl: './note-dialog.component.html',
  styleUrl: './note-dialog.component.scss'
})
export class NoteDialogComponent {
  readonly dialogRef = inject(MatDialogRef<NoteDialogComponent>);
  readonly data = inject<NoteData>(MAT_DIALOG_DATA);
  readonly note = model(this.data.content);

  onNoClick(): void{
    this.dialogRef.close();
  }

  save(): void {
    this.dialogRef.close({ content: this.note() });
  }
}
