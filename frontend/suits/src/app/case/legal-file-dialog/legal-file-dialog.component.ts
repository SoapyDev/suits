import {ChangeDetectionStrategy, Component, inject, model, signal} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef
} from "@angular/material/dialog";
import {MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInputModule} from '@angular/material/input';
import {Case} from '../models/case.model';
import {MatOption, MatSelect} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import {merge} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {MatCard} from '@angular/material/card';
import {CaseService} from '../services/case.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-legal-file-dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatFormFieldModule,
    MatLabel,
    MatInputModule,
    MatSelect,
    MatOption,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatCard,
  ],
  templateUrl: './legal-file-dialog.component.html',
  styleUrl: './legal-file-dialog.component.scss',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true
})
export class LegalFileDialogComponent {

  readonly dialogRef = inject(MatDialogRef<LegalFileDialogComponent>);

  protected readonly firstname = new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]);
  protected firstnameErrorMessage = signal('');

  protected readonly lastname = new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]);
  protected lastnameErrorMessage = signal('');

  protected readonly email = new FormControl('', [Validators.required, Validators.email, Validators.maxLength(255)]);
  protected emailErrorMessage = signal('');

  protected readonly phone = new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(20), Validators.pattern('^[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$')]);
  protected phoneErrorMessage = signal('');

  protected readonly civicNumber = new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(10), Validators.pattern('^[0-9]{1,10}$')]);
  protected civicNumberErrorMessage = signal('');

  protected readonly streetName = new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]);
  protected streetNameErrorMessage = signal('');

  protected readonly city = new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]);
  protected cityErrorMessage = signal('');

  protected readonly postalcode = new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(7), Validators.pattern('^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$')]);
  protected postalCodeErrorMessage = signal('');

  protected readonly birthdate = new FormControl('', [Validators.required]);
  protected birthdateErrorMessage = signal('');

  protected startDate = model<Date>(new Date(Date.now()));
  protected endDate = model<Date>(new Date(Date.now()));

  protected province = signal('Québec');
  protected lawField = signal('CIVIL');
  protected paymentAgreement = signal('HORAIRE');


  public onNoClick(): void{
    this.dialogRef.close();
  }

  protected async createLegalFile() {

    const legalCase : Case = {
      id: '',
      firstName: this.firstname.value!,
      lastName: this.lastname.value!,
      email: this.email.value!,
      phoneNumber: this.phone.value!,
      civicNumber: this.civicNumber.value!,
      streetName: this.streetName.value!,
      city: this.city.value!,
      province: this.province(),
      postalCode: this.postalcode.value!,
      birthdate: this.birthdate.value!,
      description: '',
      status: 'NON_DEBUTE',
      startOfMandate: this.startDate(),
      endOfMandate: this.endDate(),
      isClosed: false,
      lawField: this.lawField(),
      agreement: this.paymentAgreement(),
      legalFileTeamMembers: []
    };

    this.createCase(legalCase).then(v => this.dialogRef.close());
  }


  constructor(protected service: CaseService, protected router : Router) {
    merge(this.firstname.statusChanges, this.firstname.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateFirstnameMessage());

    merge(this.lastname.statusChanges, this.lastname.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateLastnameMessage());

    merge(this.birthdate.statusChanges, this.birthdate.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateBirthdateMessage());

    merge(this.email.statusChanges, this.email.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateEmailMessage());

    merge(this.phone.statusChanges, this.phone.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePhoneMessage());

    merge(this.civicNumber.statusChanges, this.civicNumber.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateCivicNumberMessage());

    merge(this.streetName.statusChanges, this.streetName.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateStreetNameMessage());

    merge(this.city.statusChanges, this.city.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateCityMessage());

    merge(this.postalcode.statusChanges, this.postalcode.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePostalCodeMessage());
  }

  private updateFirstnameMessage() {
    if (this.firstname.hasError('required')) {
      this.firstnameErrorMessage.set('Ce champ est requis');
    } else if (this.firstname.hasError('minlength')) {
      this.firstnameErrorMessage.set(`Doit contenir au moins deux caractères`);
    } else if (this.firstname.hasError('maxlength')) {
      this.firstnameErrorMessage.set('Ne peut avoir plus de 255 caractères');
    }else{
      this.firstnameErrorMessage.set('');
    }
  }

  private updateLastnameMessage() {
    if (this.lastname.hasError('required')) {
      this.lastnameErrorMessage.set('Ce champ est requis');
    } else if (this.lastname.hasError('minlength')) {
      this.lastnameErrorMessage.set(`Doit contenir au moins deux caractères`);
    } else if (this.lastname.hasError('maxlength')) {
      this.lastnameErrorMessage.set('Ne peut avoir plus de 255 caractères');
    }else{
      this.lastnameErrorMessage.set('');
    }
  }

  private updateBirthdateMessage() {
    if (this.birthdate.hasError('required')) {
      this.birthdateErrorMessage.set('Ce champ est requis');
    } else {
      this.birthdateErrorMessage.set('');
    }
  }

  private updateEmailMessage() {
    if (this.email.hasError('required')) {
      this.emailErrorMessage.set('Ce champ est requis');
    } else if (this.email.hasError('email')) {
      this.emailErrorMessage.set(`${this.email.value} n'est pas un courriel valide`);
    } else {
      this.emailErrorMessage.set('');
    }
  }

  private updatePhoneMessage() {
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

  private updateCivicNumberMessage() {
    if (this.civicNumber.hasError('required')) {
      this.civicNumberErrorMessage.set('Ce champ est requis');
    } else if (this.civicNumber.hasError('minlength')) {
      this.civicNumberErrorMessage.set(`Doit contenir au moins 1 caractère`);
    }else if (this.civicNumber.hasError('pattern')){
      this.civicNumberErrorMessage.set('Doit contenir au plus 10 caractères numériques');
    }else{
      this.civicNumberErrorMessage.set('');
    }
  }

  private updateStreetNameMessage() {
    if (this.streetName.hasError('required')) {
      this.streetNameErrorMessage.set('Ce champ est requis');
    } else if (this.streetName.hasError('minlength')) {
      this.streetNameErrorMessage.set(`Doit contenir au moins deux caractères`);
    } else if (this.streetName.hasError('maxlength')) {
      this.streetNameErrorMessage.set('Ne peut avoir plus de 255 caractères');
    }else{
      this.streetNameErrorMessage.set('');
    }
  }

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


  async createCase(legalCase: Case){

    try{
      const res = await this.service.createCase(legalCase)
      await this.router.navigate(['/app/cases/', res.id]);
    }catch (error){
      console.log(error);
    }

  }
}
