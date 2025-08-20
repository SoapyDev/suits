import {Priority} from './priority.enum';

export interface TaskHeader {
  caseId: string;
  taskId: number,
  name: string,
  startDate: string,
  dueDate: string,
  status: string,
  assignee: string,
  priority: Priority,
}
