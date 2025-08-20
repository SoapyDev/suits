import {Component} from '@angular/core';
import {CaseHeader} from '../../../case/models/case-header.model';
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from '@angular/material/expansion';
import {TaskService} from "../../service/task.service";
import {Task} from "../../models/task.model";
import {from, Observable, of} from "rxjs";
import { map } from 'rxjs/operators';
import {AsyncPipe, NgForOf} from "@angular/common";
import {CaseService} from '../../../case/services/case.service';

@Component({
  selector: 'app-task-listing',
  imports: [
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionPanelHeader,
    AsyncPipe,
    NgForOf,
  ],
  templateUrl: './task-listing.component.html',
  styleUrl: './task-listing.component.scss'
})
export class TaskListingComponent {

  protected cases: CaseHeader[] = [];
  private taskCache = new Map<string, Observable<Task[]>>();


  constructor(
      private taskService: TaskService,
      private caseService: CaseService
  ) {}

  async ngOnInit() {
    this.cases = await this.caseService.getCasesHeadersByUser();
  }

  public get_tasks_by_caseId(caseId: string):Observable<Task[]> {

    if (!this.taskCache.has(caseId)){
      const taskObservable = from(this.taskService.getTaskByCaseId(caseId)).pipe(
        map(tasks => (tasks ?? []).filter(task => task.legalFileId === caseId))
      );
      this.taskCache.set(caseId, taskObservable);
    }

    return this.taskCache.get(caseId) ?? of([]);
  }

  trackByTaskId(index: number, task: Task): number {
    return task.id!;
  }

  protected formatDate(value : string){
    const values = value.toString().split(',');
    const day = values[2].padStart(2, '0');
    const month = values[1].padStart(2, '0');
    return `${values[0]}-${month}-${day}`;
  }

  formatTitle(title: string) {
    return title.length < 30 ? title : title.substring(0, 25) + "...";
  }
}
