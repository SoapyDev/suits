import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, signal} from '@angular/core';
import {SiteNavComponent} from '../site-nav/site-nav.component';
import {SiteNavPickerComponent} from '../site-nav-picker/site-nav-picker.component';
import {NgOptimizedImage} from '@angular/common';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {merge} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  MatDatepickerModule,
} from '@angular/material/datepicker';
import {MatOption, provideNativeDateAdapter} from '@angular/material/core';
import {ProfileService} from './profile.service';
import {MatFabButton} from '@angular/material/button';
import {Profile} from './profile.model';
import {MatSelect} from '@angular/material/select';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-profile',
  imports: [
    SiteNavComponent,
    SiteNavPickerComponent,
    NgOptimizedImage,
    MatFormField,
    MatDatepickerModule,
    MatInput,
    ReactiveFormsModule,
    MatError,
    MatLabel,
    MatFabButton,
    MatSelect,
    MatOption,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  providers: [provideNativeDateAdapter()],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ProfileComponent {

  private _snackBar = inject(MatSnackBar);

  protected profession = signal('');
  protected readonly name = new FormControl('',
    [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(75),
    ]);
  protected nameErrorMessage = signal('');

  updateNameErrorMessage() {
    if (this.name.hasError('required')){
      this.nameErrorMessage.set('Nom est requis');
    } else if (this.name.hasError('minlength')) {
      this.nameErrorMessage.set('Doit contenir au moins 2 charactères')
    }else if (this.name.hasError('maxLength')){
      this.nameErrorMessage.set('Ne peut avoir plus de 75 charactères.')
    }else{
      this.nameErrorMessage.set('')
    }
  }


  protected readonly email = new FormControl('',
    [
      Validators.required,
      Validators.email
    ]);
  protected emailErrorMessage = signal('');

  updateEmailErrorMessage() {
   if (this.email.hasError('required')){
     this.emailErrorMessage.set('Courriel est requis')
   } else if (this.email.hasError('email')){
     this.emailErrorMessage.set('Requiert un courriel valide')
   }else{
     this.emailErrorMessage.set('')
   }
  }

  protected readonly phone = new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(20), Validators.pattern('^[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$')]);
  protected phoneErrorMessage = signal('');


  protected updatePhoneErrorMessage() {
    if (this.phone.hasError('required')) {
      this.phoneErrorMessage.set('Ce champ est requis');
    } else if (this.phone.hasError('minlength')) {
      this.phoneErrorMessage.set(`Doit contenir au moins 10 caractères`);
    } else if (this.phone.hasError('maxlength')) {
      this.phoneErrorMessage.set('Ne peut avoir plus de 20 caractères');
    }else if (this.phone.hasError('pattern')){
      this.phoneErrorMessage.set('Doit respecter le format 123 123 1234');
    }else{
      this.phoneErrorMessage.set('');
    }
  }

  protected readonly address = new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]);
  protected addressErrorMessage = signal('');

  private updateAddressMessage() {
    if (this.address.hasError('required')) {
      this.addressErrorMessage.set('Ce champ est requis');
    } else if (this.address.hasError('minlength')) {
      this.addressErrorMessage.set(`Doit contenir au moins deux caractères`);
    } else if (this.address.hasError('maxlength')) {
      this.addressErrorMessage.set('Ne peut avoir plus de 255 caractères');
    }else{
      this.addressErrorMessage.set('');
    }
  }

  protected readonly city = new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]);
  protected cityErrorMessage = signal('');

  private updateCityMessage() {
    if (this.city.hasError('required')) {
      this.cityErrorMessage.set('Ce champ est requis');
    } else if (this.city.hasError('minlength')) {
      this.cityErrorMessage.set(`Doit contenir au moins deux caractères`);
    } else if (this.city.hasError('maxlength')) {
      this.cityErrorMessage.set('Ne peut avoir plus de 255 caractères');
    }else{
      this.cityErrorMessage.set('');
    }
  }

  protected readonly postalcode = new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(7), Validators.pattern('^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$')]);
  protected postalCodeErrorMessage = signal('');

  private updatePostalCodeMessage() {
    if (this.postalcode.hasError('required')) {
      this.postalCodeErrorMessage.set('Ce champ est requis');
    } else if (this.postalcode.hasError('minlength')) {
      this.postalCodeErrorMessage.set(`Doit contenir au moins 6 caractères`);
    } else if (this.postalcode.hasError('maxlength')) {
      this.postalCodeErrorMessage.set('Ne peut avoir plus de 7 caractères');
    }else if (this.postalcode.hasError('pattern')){
      this.postalCodeErrorMessage.set('Doit respecter le format A1A A1A');
    }else{
      this.postalCodeErrorMessage.set('');
    }
  }

  protected readonly birthdate = new FormControl(new Date(), [Validators.required]);
  protected birthdateErrorMessage = signal('');

  private updateBirthdateMessage() {
    if (this.filePath.hasError('required')) {
      this.filePathErrorMessage.set('Ce champ est requis');
    } else {
      this.filePathErrorMessage.set('');
    }
  }


  protected readonly filePath = new FormControl('', []);
  protected filePathErrorMessage = signal('');

  async ngOnInit() {
    await this.profileService.getCurrentProfile().then((profile) => {
        this.name.patchValue(profile.name);
        this.email.patchValue(profile.username);
        this.phone.patchValue(profile.phoneNumber);
        this.address.patchValue(profile.address);
        this.city.patchValue(profile.city);
        this.postalcode.patchValue(profile.postalCode);
        this.profession.set(profile.profession);
        this.filePath.patchValue(profile.profilePicturePath);
        this.birthdate.patchValue(new Date(profile.birthdate));
      }
    );
  }

  constructor(protected profileService: ProfileService) {
    merge(this.name.statusChanges, this.name.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateNameErrorMessage());

    merge(this.email.statusChanges, this.email.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateEmailErrorMessage());

    merge(this.phone.statusChanges, this.phone.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePhoneErrorMessage());

    merge(this.filePath.statusChanges, this.filePath.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateBirthdateMessage());

    merge(this.address.statusChanges, this.address.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateAddressMessage());

    merge(this.city.statusChanges, this.city.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateCityMessage());

    merge(this.postalcode.statusChanges, this.postalcode.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePostalCodeMessage());
  }

  protected getProfessionValue(value: string) {
    switch (value){
      case "AVOCAT" : return "Avocat(e)";
      case "ADJOINT_JURIDIQUE" : return "Adjoint(e) juridique";
      case "STAGIAIRE" : return "Stagiaire";
      case "JUGE" : return "Juge";
      case "NOTAIRE" : return "Notaire";
      case "SECRETARIAT" : return "Secretariat";
      case "COMPTABLE" : return "Comptable";
      default: return value;
    }
  }


  async updateProfile() {
    const profile = this.getFormProfileValues();
    const res = await this.profileService.postProfile(profile);
    if (res.name.length <= 0){
      this._snackBar.open(
        "Une erreur est survenu durant la mise à jours du profile. " +
        "Veuillez réessayer ou contacter votre administrateur si le problème persiste.",
        '',
        {duration: 5000, horizontalPosition: "center", verticalPosition: "top"}
      );
    }
  }

  private getFormProfileValues(): Profile {
    return {
      name: this.name.getRawValue()!,
      username: this.email.getRawValue()!,
      address: this.address.getRawValue()!,
      birthdate: this.birthdate.getRawValue()!,
      city: this.city.getRawValue()!,
      country: 'Canada',
      phoneNumber: this.phone.getRawValue()!,
      postalCode: this.postalcode.getRawValue()!,
      profession: this.profession(),
      profilePicturePath: this.filePath.getRawValue()!,
      province: 'Québec',
    }
  }

  protected isInvalideForm() : boolean {
    return this.nameErrorMessage().length > 0
      || this.emailErrorMessage().length > 0
      || this.birthdateErrorMessage().length > 0
      || this.phoneErrorMessage().length > 0
      || this.addressErrorMessage().length > 0
      || this.cityErrorMessage().length > 0
      || this.postalCodeErrorMessage().length > 0
  }
}
