import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {environment} from '../../../environments/environment';
import {UserProfile} from '../../dashboard-pro/models/user-profile.model';
import {teamMember} from '../models/team-member.model';

@Injectable({
  providedIn: 'root'
})
export class MemberDialogService {

  constructor(private httpClient: HttpClient) { }

  async getAllUsersDTO(): Promise<teamMember[]|null> {

    return await firstValueFrom(this.httpClient.get<teamMember[]>
    (`${environment.backendUrl}/api/users`,
      {withCredentials: true}));
  }

  async getAllUsers(): Promise<UserProfile[]|null> {

    return await firstValueFrom(this.httpClient.get<UserProfile[]>
    (`${environment.backendUrl}/api/users`,
      {withCredentials: true}));
  }
}
