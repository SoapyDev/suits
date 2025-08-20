import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  CUSTOM_ELEMENTS_SCHEMA,
  inject,
  OnInit,
  signal
} from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {MatDivider} from '@angular/material/divider';
import {Task} from '../../models/task.model'
import {SiteNavComponent} from '../../../site-nav/site-nav.component';
import {SiteNavPickerComponent} from '../../../site-nav-picker/site-nav-picker.component';
import {MatButton, MatIconButton} from '@angular/material/button';
import {NoteComponent} from '../../../note/note.component';
import {LoggedTimeComponent} from '../../../logged-time/logged-time.component';
import {StatusComponent} from '../../../status/status.component';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {MatFormField, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatOption, provideNativeDateAdapter} from '@angular/material/core';
import {NoteDialogComponent} from '../../../note-dialog/note-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {TimeDialogComponent} from '../../../time-dialog/time-dialog.component';
import {ActivatedRoute} from "@angular/router";
import {TaskService} from "../../service/task.service";
import {CommonModule, DatePipe, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {UserProfile} from "../../../dashboard-pro/models/user-profile.model";
import {MatSelect} from "@angular/material/select";
import {TaskTime} from "../../models/task-times.model";
import {priorityToString} from '../../models/priority.enum';
import {NoteRequest} from '../../../note/model/note-request.model';
import {firstValueFrom} from 'rxjs';

export interface NoteData{
  content: string;
}

export interface TimeData{
  duration : number;
  value : number;
}

@Component({
  selector: 'app-task-detail',
  imports: [
    MatIcon,
    MatDivider,
    SiteNavComponent,
    SiteNavPickerComponent,
    MatIconButton,
    NoteComponent,
    LoggedTimeComponent,
    StatusComponent,
    MatMenuTrigger,
    MatMenu,
    MatMenuItem,
    MatButton,
    MatFormField,
    MatInput,
    NgIf,
    FormsModule,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDatepicker,
    MatSuffix,
    DatePipe,
    MatSelect,
    MatOption,
    CommonModule
  ],
  providers: [provideNativeDateAdapter()],
  templateUrl: './task-detail.component.html',
  styleUrl: './task-detail.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  changeDetection: ChangeDetectionStrategy.OnPush,

})
export class TaskDetailComponent implements OnInit{

  protected task: Task | null = null;
  protected isLoading: boolean = true;
  protected errorMessage: string | null = null;
  protected taskId!: string;
  protected teamMembers: UserProfile[] = [];

  protected duration = signal(0);
  protected value = signal(0);

  protected noteContent = signal('');
  readonly dialog = inject(MatDialog);

  constructor(
      private route: ActivatedRoute,
      private taskService: TaskService,
      private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadTask().then()

  }

  private async loadTask(): Promise<void> {
    this.taskId = this.route.snapshot.paramMap.get('taskId')!;
    if (!this.taskId) {
      this.errorMessage = "Le ID de la tâche n'a pas pu être récupéré.";
      this.isLoading = false;
      this.cd.markForCheck();
      return;
    }
    this.taskService.getTaskById(+this.taskId).then(task => {
      this.task = task;
      this.isLoading = false;
      this.cd.markForCheck();

      this.taskService.getTeamMembersFromCaseId(task.legalFileId!).then(members => {
        this.teamMembers = members;
        this.cd.markForCheck();
      });
    }).catch(() => {
      this.errorMessage = "Erreur lors de la récupération de la tâche.";
      this.isLoading = false;
      this.cd.markForCheck();
    });
  }


  openTimeDialog(): void {
    const dialogRef = this.dialog.open(TimeDialogComponent, {
      width: '100%',
      maxWidth: '600px',
      height: "100%",
      maxHeight: '300px',
      data: {duration: this.duration(), value: this.value},
    });

    dialogRef.afterClosed().subscribe((result: TimeData | undefined) => {
      if (result) {
        const newTaskTime: TaskTime = {
          task_id: 0,
          id:-1,
          name: this.task?.assignee.name!,
          time: result.duration,
          tarification: result.value
        };

        this.onFieldChange('taskTimes', newTaskTime);
      }
    });
  }

  async openNoteDialog(): Promise<void> {
    const dialogRef = this.dialog.open(NoteDialogComponent, {
      width: '100%',
      maxWidth: '600px',
      height: "100%",
      maxHeight: '400px',
      data: { content: this.noteContent() },
    });

    const result: NoteData | undefined = await firstValueFrom(dialogRef.afterClosed());

    if (result) {
      const noteRequest: NoteRequest = { content: result.content };

      try {
        const newNote = await this.taskService.createNote(this.taskId, noteRequest);
        if(this.task?.taskNotes){
          this.task.taskNotes.push(newNote);
          this.cd.markForCheck();
        }

      } catch (error) {
        console.error("Erreur lors de l'ajout de la note :", error);

      }
    }
  }

  updateNote(event: { id: number, content: string }): void {
    const noteRequest: NoteRequest = { content: event.content };

    this.taskService.updateNote(this.taskId, event.id, noteRequest).then((updatedNote) => {
      if(this.task?.taskNotes){
        const index = this.task.taskNotes.findIndex(n => n.id === updatedNote.id);
        if (index !== -1) {
          this.task.taskNotes[index] = updatedNote;
          this.cd.markForCheck();
        }
      }
    }).catch((err:any) => {
      console.error("Erreur lors de la mise à jour de la note :", err);
    });
  }

  deleteNote(noteId: number): void {
    this.taskService.deleteNote(this.taskId, noteId).then(() => {
      if(this.task?.taskNotes){
        this.task.taskNotes = this.task.taskNotes.filter(note => note.id !== noteId);
        this.cd.markForCheck();
      }
    }).catch((err: any) => {
      console.error("Erreur lors de la suppression :", err);
    });
  }



  async onFieldChange(fieldValue: string, value: any) {

    try {
      const updatedField = { [fieldValue]: value };
      const updatedTask = await this.taskService.patchTask(this.taskId, updatedField);

      if (updatedTask){
        this.task = updatedTask;
        this.cd.detectChanges();
      }
    }catch (error){
      console.error("Problème lors de la mise à jour de la tâche : ", error)
    }

  }

  protected readonly priorityToString = priorityToString;

  getTeamMembersByName(value: string): UserProfile | undefined {
    return this.teamMembers.find(teamMember => teamMember.name === value)
  }
}
