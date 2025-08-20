import {Component, ViewChild} from '@angular/core';
import {MatSort, MatSortHeader} from '@angular/material/sort';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {StatusComponent} from '../status/status.component';
import {Task} from '../task/models/task.model';
import {TaskService} from '../task/service/task.service';
import {NgIf} from '@angular/common';
import {MatIconAnchor} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {priorityToString} from '../task/models/priority.enum';

@Component({
  selector: 'app-tasks-list',
  imports: [
    MatSort,
    MatTable,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatSortHeader,
    StatusComponent,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    NgIf,
    MatIconAnchor,
    MatIcon,
  ],
  templateUrl: './tasks-list.component.html',
  styleUrl: './tasks-list.component.scss'
})
export class TasksListComponent {

  protected tasks!: Task[];
  protected dataSource!: MatTableDataSource<Task>;
  @ViewChild(MatSort) sort!: MatSort;

  protected readonly columns = ['name','case','priority', 'start','end', 'goto'];

  constructor(
    protected taskService: TaskService
  ) {}

  async ngOnInit() {
    this.tasks = await this.taskService.getAllTaskFromUser();
    this.dataSource = new MatTableDataSource<Task>(this.tasks);
    this.dataSource.data = this.tasks;
  }

  getTaskLink(id: number) {
    return `/app/tasks/${id}`;
  }

  protected formatDate(date: string) {
    if (!date) {return;}
    const elements =  date.toString().split(',');
    return elements[1] + '/' + elements[2] + '/' + elements[0];
  }

  protected readonly priorityToString = priorityToString;
}
