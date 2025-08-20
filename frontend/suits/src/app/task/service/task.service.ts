import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {environment} from '../../../environments/environment.development';
import {Task} from '../models/task.model'
import { Router } from '@angular/router';
import {UserProfile} from "../../dashboard-pro/models/user-profile.model";
import {TaskNote} from '../models/task-notes.model';
import {NoteRequest} from '../../note/model/note-request.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(
      private httpClient: HttpClient,
      private router: Router
  ) { }

  async getTaskById(taskId: number) : Promise<Task>  {

    try {
      return  await firstValueFrom(
        this.httpClient.get<Task>(`${environment.backendUrl}/api/tasks/${taskId}`, {
          withCredentials: true }
        )
      );

    }catch (error){
      console.error("Erreur lors de la récupération de la tâche : ", error);
      return this.createEmptyTask();
    }

  }

  async getTaskByCaseId(caseID: string): Promise<Task[]>  {
    try {
      return await firstValueFrom(
        this.httpClient.get<Task[]>(`${environment.backendUrl}/api/legalFile/${caseID}/tasks`, {
          withCredentials: true }
        )
      );

    }catch (error){
      console.error("Erreur lors de la récupération des tâches : ", error);
      return [];
    }
  }

  async getAllTaskFromUser(): Promise<Task[]>  {
    try {
      return await firstValueFrom(
        this.httpClient.get<Task[]>(`${environment.backendUrl}/api/user/tasks`, {
          withCredentials: true}
        )
      );

    }catch (error){
      console.error("Erreur lors de la récupération des tâches : ", error);
      return [];
    }
  }

  async getTeamMembersFromCaseId(caseId: string): Promise<UserProfile[]>  {
      return await firstValueFrom(
          this.httpClient.get<UserProfile[]>(`${environment.backendUrl}/api/dossier/${caseId}/teammembers`, {
            withCredentials: true }));

  }

  async createTask(caseId: string) : Promise<void> {

    let newTask = this.createEmptyTask();
    newTask.legalFileId = caseId;
    newTask.assignee.username = localStorage.getItem("username");

    try {
      const createdTask = await firstValueFrom(
        this.httpClient.post<Task>(`${environment.backendUrl}/api/tasks`,
          newTask,
          { withCredentials: true })
      );
      await this.router.navigate(['/app/tasks/', createdTask.id]);


    }catch (error){
      console.error("Erreur lors de la création de la tâche : ", error);
    }
  }

  async patchTask(taskId:string, updatedField: any) : Promise<Task | null>{

    try {
      return await firstValueFrom(
        this.httpClient.patch<Task>(`${environment.backendUrl}/api/tasks/${taskId}`,
          updatedField,
          { withCredentials: true })
      );

    }catch (error){
      console.error("Erreur lors de la mise à jour de la tâche : ", error);
      return null;
    }
  }

  createNote(taskId: string, noteRequest: NoteRequest): Promise<TaskNote> {
    return firstValueFrom(
        this.httpClient.post<TaskNote>(
          `${environment.backendUrl}/api/tasks/${taskId}/notes`,
          noteRequest,
          { withCredentials: true })
      );
  }

  updateNote(taskId: string, noteId: number, noteRequest: NoteRequest ): Promise<TaskNote>{
    return firstValueFrom(
      this.httpClient.put<TaskNote>(
        `${environment.backendUrl}/api/tasks/${taskId}/notes/${noteId}`,
        noteRequest,
        { withCredentials: true })
      );
  }

  deleteNote(taskId: string, noteId: number): Promise<void> {
    return firstValueFrom(
      this.httpClient.delete<void>(
        `${environment.backendUrl}/api/tasks/${taskId}/notes/${noteId}`,
        { withCredentials: true }
      )
    );
  }

  public createEmptyTask(): Task {
    return {
      "id":null,
      "legalFileId": null,
      "title": null,
      "description": null,
      "assignee":{
          address : null,
          birthdate : null,
          city : null,
          country : null,
          name : null,
          phoneNumber : null,
          postalCode : null,
          profession : null,
          profilePicturePath : null,
          province : null,
          username : null
    },
      "status": null,
      "priority": 3,
      "startDate": null,
      "dueDate": null,
      "taskTimes": null,
      "taskNotes": null
    };

  }

}
