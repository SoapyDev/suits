import {Component, EventEmitter, Input, Output, output, signal} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader} from '@angular/material/card';
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatFabButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatInput} from '@angular/material/input';
import {merge} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {RegistrationCredentials} from '../model/registration-credentials';

@Component({
  selector: 'app-register',
  imports: [
    FormsModule,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatError,
    MatFabButton,
    MatFormField,
    MatHint,
    MatIcon,
    MatIconButton,
    MatInput,
    MatLabel,
    MatSuffix,
    ReactiveFormsModule,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  standalone: true
})
export class RegisterComponent {
  protected readonly email = new FormControl('', [Validators.required, Validators.email]);
  protected emailErrorMessage = signal('');

  protected readonly name = new FormControl('',
    [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(255),
    ]);
  protected nameErrorMessage = signal('');

  protected hide = signal(true);
  protected readonly password = new FormControl(
    '',
    [
      Validators.required,
      Validators.minLength(12),
      Validators.maxLength(255),
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{12,}$')]
  );
  protected passwordErrorMessage = signal('');

  protected hideConfirm = signal(true);
  protected readonly confirmPassword = new FormControl(
    '',
    [
      Validators.required,
      this.validateConfirmPassword(),
    ]
  );

  validateConfirmPassword(): ValidatorFn {
    return (control: AbstractControl) : ValidationErrors | null => {

      const value = control.value;

      if (!value){
        return null;
      }

      const equalsPassword = control.value === this.password.value;
      return !equalsPassword ? {passwordConfirm:true}: null;

    }
  }

  protected confirmPasswordErrorMessage = signal('');


  register = output<RegistrationCredentials>();

  protected formCredentials: RegistrationCredentials = {
    username:'',
    password:'',
    name: '',
  };

  @Input()
  showCredentialsError = false;
  @Input()
  showConnectionError = false;

  constructor() {
    merge(this.email.statusChanges, this.email.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateEmailErrorMessage());

    merge(this.name.statusChanges, this.name.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateNameErrorMessage());

    merge(this.password.statusChanges, this.password.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePasswordErrorMessage());

    merge(this.confirmPassword.statusChanges, this.confirmPassword.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateConfirmPasswordErrorMessage());
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
    else if (this.password.hasError('minLength')) {
      this.passwordErrorMessage.set('Ce champ doit contenir au moins 12 charactères');
    }
    else if (this.password.hasError('maxLength')) {
      this.passwordErrorMessage.set('Ce champ ne peut dépasser 255 charactères');
    }
    else if (this.password.hasError('pattern')) {
      this.passwordErrorMessage.set('Le mot de passe doit contenir au moins une lettre minuscule, majuscule, un nombre et un caractère spécial');
    }
  }

  private updateConfirmPasswordErrorMessage() {
    if (this.confirmPassword.hasError('required')) {
      this.confirmPasswordErrorMessage.set('Ce champ est requis');
    }
    else if (this.confirmPassword.hasError('passwordConfirm')) {
      this.confirmPasswordErrorMessage.set('Mot de passe et Confirmer mot de passe ne sont pas identiques');
    }
  }

  private updateNameErrorMessage() {
    if (this.name.hasError('required')) {
      this.nameErrorMessage.set('Ce champ est requis');
    } else if (this.name.hasError('minLength')) {
      this.nameErrorMessage.set(`Ce champ doit contenir au moins 2 charactères`);
    } else if (this.name.hasError('maxLength')) {
      this.nameErrorMessage.set('Ce champ ne peut dépasser 255 characteres');
    }
  }

  protected clickEventHide(event : MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
    event.preventDefault();
  }
  protected clickEventHideConfirm(event : MouseEvent) {
    this.hideConfirm.set(!this.hideConfirm());
    event.stopPropagation();
    event.preventDefault();
  }

  onRegister() {

    if (this.email.valid &&
      this.password.valid &&
      this.name.valid &&
      this.email.value != null &&
      this.name.value !== null &&
      this.password.value != null) {

      this.formCredentials.username = <string>this.email.value;
      this.formCredentials.password = <string>this.password.value;
      this.formCredentials.name = <string>this.name.value;
      this.register.emit(this.formCredentials);
    }
  }
}
