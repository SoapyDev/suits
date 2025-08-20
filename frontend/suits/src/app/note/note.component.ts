import {Component, CUSTOM_ELEMENTS_SCHEMA, EventEmitter, Input, Output, signal} from '@angular/core';
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TaskNote} from '../task/models/task-notes.model';
import {FormsModule} from '@angular/forms';
import {NgClass, NgIf} from '@angular/common';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-note',
  imports: [
    MatFormField,
    MatInput,
    FormsModule,
    NgClass,
    NgIf,
    MatIcon,
    MatIconButton,
    MatTooltip
  ],
  templateUrl: './note.component.html',
  styleUrl: './note.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class NoteComponent {

  @Input() note! : TaskNote;

  @Output() noteUpdated = new EventEmitter<{ id: number; content: string }>();
  @Output() noteDeleted = new EventEmitter<number>();

  protected isReadonly = signal(true);
  protected isOwner = false;

  ngOnInit() {
    const currentUsername = localStorage.getItem('username');
    if (currentUsername && currentUsername === this.note.creator.username) {
      this.isReadonly.set(false);
      this.isOwner = true;
    }
  }

  formatStringDate(dateString: string | null | undefined): string {
    if (!dateString) return 'Date inconnue';

    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Date invalide';

    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  }

  formatProfession(profession: string | null | undefined): string {
    if(!profession) return "";

    return "  -  " + profession.charAt(0).toUpperCase() + profession.slice(1).toLowerCase();
  }

  onBlur(): void {
    if (!this.isReadonly()) {
      this.noteUpdated.emit({ id: this.note.id, content: this.note.content });
    }
  }

  onDelete(): void {
      this.noteDeleted.emit(this.note.id);
  }
}
