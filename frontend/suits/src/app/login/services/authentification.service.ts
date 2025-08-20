import { Injectable, signal } from '@angular/core';
import { UserCredentials } from '../model/user-credentials';
import { HttpClient } from '@angular/common/http';
import { LoginResponse } from '../model/login-response';
import { environment } from '../../../environments/environment.development';
import { firstValueFrom } from 'rxjs';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  static KEY = 'username';

  private username = signal<string | null>(null);

  constructor(private httpClient: HttpClient,
              private router: Router) {
    this.username.set(localStorage.getItem(AuthenticationService.KEY));
  }

  async login(userCredentials: UserCredentials) {

    const userCredentialsData = await firstValueFrom(
      this.httpClient.post<LoginResponse>(
        `${environment.backendUrl}/login`,
        userCredentials,
        { withCredentials: true }
      ));

    this.username.set(userCredentialsData!.username);
    localStorage.setItem(AuthenticationService.KEY, userCredentialsData!.username);
    localStorage.setItem("role", userCredentialsData!.role);
  }

  logout(){

    this.httpClient.post<LoginResponse>(`${environment.backendUrl}/logout`,
      { withCredentials: true }
    ).subscribe({
        next: () => {
          localStorage.clear();
          this.router.navigate(['/']);
        },
        error: err => {
          console.error('Erreur lors du logout', err);
        }
      });
  }

}
