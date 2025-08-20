import {Priority} from './priority.enum';
import {TaskTime} from "./task-times.model";
import {TaskNote} from "./task-notes.model";

export interface Task {
  id: number | null,
  legalFileId: string | null,
  title: string | null,
  description: string | null,
  assignee: {
    address : string | null,
    birthdate : string | null,
    city : string | null,
    country : string | null,
    name : string | null,
    phoneNumber : string | null,
    postalCode : string | null,
    profession : string | null,
    profilePicturePath : string | null,
    province : string | null,
    username : string | null
  },
  status: string | null,
  priority: Priority | null,
  startDate: string | null,
  dueDate: string | null,
  taskTimes: TaskTime[] | null,
  taskNotes: TaskNote[] | null
}
