import {Component, inject, signal} from '@angular/core';
import {Router} from '@angular/router';
import {RegistrationService} from '../../services/registration.service';
import {MediaMatcher} from '@angular/cdk/layout';
import {RegistrationCredentials} from '../../model/registration-credentials';
import {HttpErrorResponse} from '@angular/common/http';
import {RegisterComponent} from '../../component/register.component';
import {SiteNavComponent} from '../../../site-nav/site-nav.component';

@Component({
  selector: 'app-register-page-client',
  imports: [RegisterComponent, SiteNavComponent],
  templateUrl: './register-page-client.component.html',
  styleUrl: './register-page-client.component.scss',
  standalone: true
})
export class RegisterPageClientComponent {

  showCredentialsError: boolean;
  showConnectionError: boolean;

  protected readonly isMobile = signal(true);
  private readonly _mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;

  constructor(private router: Router,
              private registrationService: RegistrationService) {

    const media = inject(MediaMatcher);

    this.showCredentialsError = false;
    this.showConnectionError = false;
    this._mobileQuery = media.matchMedia('(max-width: 600px)');
    this.isMobile.set(this._mobileQuery.matches);
    this._mobileQueryListener = () => this.isMobile.set(this._mobileQuery.matches);
    this._mobileQuery.addEventListener('change', this._mobileQueryListener);

  }

  async onRegister(registrationData: RegistrationCredentials) {

    try{
      await this.registrationService.registerClient(registrationData);
      //TODO changer la redirection au dashboard une fois que la page dashboard est cree
      await this.router.navigate(['/login']);
    } catch (error) {
      if (error instanceof HttpErrorResponse) {
        if(error.status == 403){
          this.showCredentialsError = true;
        } else {
          this.showConnectionError = true;
        }
      }
    }
  }
}
