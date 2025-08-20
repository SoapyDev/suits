import {Injectable, signal} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegistrationCredentials} from '../model/registration-credentials';
import { RegistrationResponse} from '../model/registration-response';
import {firstValueFrom} from 'rxjs';
import { environment } from '../../../environments/environment.development';


@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  static KEY = 'username';

  private username = signal<string | null>(null);

  constructor(private httpClient: HttpClient) {

  }

  async registerProfessional(registrationCredentials: RegistrationCredentials): Promise<void> {

    const registrationCredentialsData =
      await firstValueFrom(this.httpClient.put<RegistrationResponse>
      (`${environment.backendUrl}/register/professional`,
        registrationCredentials, {withCredentials: true}));

    this.username.set(registrationCredentialsData!.username);

    localStorage.setItem(RegistrationService.KEY, registrationCredentialsData!.username);

  }

  async registerClient(registrationCredentials: RegistrationCredentials): Promise<void> {

    const registrationCredentialsData =
      await firstValueFrom(this.httpClient.put<RegistrationResponse>
      (`${environment.backendUrl}/register/client`,
        registrationCredentials, {withCredentials: true}));

    this.username.set(registrationCredentialsData!.username);

    localStorage.setItem(RegistrationService.KEY, registrationCredentialsData!.username);

  }



}


