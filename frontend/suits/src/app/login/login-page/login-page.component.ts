import {Component, inject, signal} from '@angular/core';
import {MediaMatcher} from '@angular/cdk/layout';
import {LoginComponent} from '../component/login.component';
import {SiteNavComponent} from '../../site-nav/site-nav.component';
import {UserCredentials} from '../model/user-credentials';
import {AuthenticationService} from '../services/authentification.service';
import {Router } from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-login-page',
  imports: [
    LoginComponent,
    SiteNavComponent,
  ],
  templateUrl: './login-page.component.html',
  standalone: true,
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {
  title = 'Connexion';
  showCredentialsError: boolean;
  showConnectionError: boolean;

  protected readonly isMobile = signal(true);
  private readonly _mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
    ) {
    const media = inject(MediaMatcher);

    this.showCredentialsError = false;
    this.showConnectionError = false;
    this._mobileQuery = media.matchMedia('(max-width: 600px)');
    this.isMobile.set(this._mobileQuery.matches);
    this._mobileQueryListener = () => this.isMobile.set(this._mobileQuery.matches);
    this._mobileQuery.addEventListener('change', this._mobileQueryListener);
  }

  async onLogin(userCredentials: UserCredentials) {
    try{
      await this.authenticationService.login(userCredentials);

      const role = localStorage.getItem("role");
      if(role == "CLIENT" || role == "PROFESSIONNEL" ){
        await this.router.navigate(['/app/tasks']);
      }

    } catch(error){
      if(error instanceof HttpErrorResponse){
        if(error.status == 403){
          this.showCredentialsError = true;
        }else{
          this.showConnectionError = true;
        }
      }
    }
  }

  }

