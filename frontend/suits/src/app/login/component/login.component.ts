import {Component, Input, output, signal} from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {FormsModule,FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import {merge} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {MatIcon} from '@angular/material/icon';
import {UserCredentials} from '../model/user-credentials';

@Component({
  selector: 'app-login',
  imports: [MatCardModule, MatButtonModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatIcon,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  standalone: true
})
export class LoginComponent {
  protected readonly email = new FormControl('', [Validators.required, Validators.email]);
  protected emailErrorMessage = signal('');

  protected hide = signal(true);
  protected readonly password = new FormControl(
    '',
    [
      Validators.required,
      Validators.min(12),
      Validators.max(255),
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{12,}$')]
  );
  protected passwordErrorMessage = signal('');
  login = output<UserCredentials>();
  protected formCredentials: UserCredentials = {
    username:'',
    password:''
  };

  @Input()
  showCredentialsError = false;
  @Input()
  showConnectionError = false;

  constructor() {
    merge(this.email.statusChanges, this.email.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateEmailErrorMessage());

    merge(this.password.statusChanges, this.password.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePasswordErrorMessage());
  }

  private updateEmailErrorMessage() {
    if (this.email.hasError('required')) {
      this.emailErrorMessage.set('Ce champ est requis');
    } else if (this.email.hasError('email')) {
      this.emailErrorMessage.set(`${this.email.value} n'est pas un courriel valide`);
    } else {
      this.emailErrorMessage.set('');
    }
  }

  private updatePasswordErrorMessage() {
    if (this.password.hasError('required')) {
      this.passwordErrorMessage.set('Ce champ est requis');
    }
    else if (this.password.hasError('min')) {
      this.passwordErrorMessage.set('Ce champ doit contenir au moins 12 charactères');
    }
    else if (this.password.hasError('max')) {
      this.passwordErrorMessage.set('Ce champ ne peut dépasser 255 charactères');
    }
    else if (this.password.hasError('pattern')) {
      this.passwordErrorMessage.set('Le mot de passe doit contenir au moins une lettre minuscule, majuscule, un nombre et un caractère spécial');
    }
  }

  protected clickEvent(event : MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
    event.preventDefault();
  }

  onLogin() {

    if (this.email.valid &&
      this.password.valid &&
      this.email.value != null &&
      this.password.value != null) {

      this.formCredentials.username = <string>this.email.value;
      this.formCredentials.password = <string>this.password.value;
      this.login.emit(this.formCredentials);
    }
  }


}
