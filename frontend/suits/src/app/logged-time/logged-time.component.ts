import {Component, CUSTOM_ELEMENTS_SCHEMA, Input, signal} from '@angular/core';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';

@Component({
  selector: 'app-logged-time',
  imports: [
    MatFormField,
    MatLabel,
    MatInput
  ],
  templateUrl: './logged-time.component.html',
  styleUrl: './logged-time.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class LoggedTimeComponent {

  @Input() loggedTime!: {
    task_id: number,
    name: string,
    tarification: number,
    time: number,
  };

  protected isReadOnly = signal(true);

}
