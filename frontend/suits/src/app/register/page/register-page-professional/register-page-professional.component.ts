import {Component, inject, signal} from '@angular/core';
import {RegisterComponent} from '../../component/register.component';
import {RegistrationService} from '../../services/registration.service';
import {Router} from '@angular/router';
import {MediaMatcher} from '@angular/cdk/layout';
import {SiteNavComponent} from '../../../site-nav/site-nav.component';
import {RegistrationCredentials} from '../../model/registration-credentials';
import {HttpErrorResponse} from '@angular/common/http';


@Component({
  selector: 'app-register-page-professional',
  imports: [RegisterComponent, SiteNavComponent],
  templateUrl: './register-page-professional.component.html',
  styleUrl: './register-page-professional.component.scss',
  standalone: true
})
export class RegisterPageProfessionalComponent {

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
      await this.registrationService.registerProfessional(registrationData);
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
