import {
  Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges,
} from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatFormField, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {CommonModule} from "@angular/common";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatOption, MatSelect} from "@angular/material/select";
import {ConventionHonorairesRequest} from '../models/convention.model';
import {MatCard, MatCardContent} from '@angular/material/card';

@Component({
  selector: 'app-convention-form',
  imports: [
    MatIcon,
    MatIconButton,
    MatFormField,
    MatInput,
    FormsModule,
    MatSuffix,
    MatSelect,
    CommonModule,
    MatCardContent,
    MatCard,
    MatLabel,
    ReactiveFormsModule,
    MatOption
  ],
  templateUrl: './convention-form.component.html',
  styleUrl: './convention-form.component.scss',
  standalone: true,
})
export class ConventionFormComponent implements OnInit, OnChanges {

  @Input() conventionHonorairesRequest: ConventionHonorairesRequest | null = null;

  form!: FormGroup;

  conventionHonorairesData: ConventionHonorairesRequest = {
    legalFileId: '',
    agreement: '',
    mandat: '',
    tarifHoraire: -1,
    estimationPrix: -1,
  }

  @Output() conventionFormData = new EventEmitter<ConventionHonorairesRequest>();
  @Output() conventionFormClose = new EventEmitter<void>();

  ngOnInit() {
    this.buildForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['conventionHonorairesRequest'] && !changes['conventionHonorairesRequest'].firstChange) {
      this.buildForm();
    }
  }

  buildForm() {
    const data = this.conventionHonorairesRequest || {
      legalFileId: '',
      agreement: '',
      mandat: '',
      tarifHoraire: -1,
      estimationPrix: -1,
    };

    this.form = new FormGroup({
      mandat: new FormControl(data.mandat, [Validators.required, Validators.minLength(10), Validators.maxLength(255)]),
      agreementType: new FormControl(data.agreement, Validators.required),
      tarifHoraire: new FormControl(data.tarifHoraire, Validators.required),
      estimationPrix: new FormControl(data.estimationPrix, Validators.required)
    });
  }

  onSave(){
    if(this.form.valid && this.conventionHonorairesRequest?.legalFileId != null){
      this.conventionHonorairesData.agreement = this.form.value.agreementType;
      this.conventionHonorairesData.mandat = this.form.value.mandat;
      this.conventionHonorairesData.legalFileId = this.conventionHonorairesRequest.legalFileId;
      this.conventionHonorairesData.tarifHoraire = Number(this.form.value.tarifHoraire);
      this.conventionHonorairesData.estimationPrix = Number(this.form.value.estimationPrix);
      this.conventionFormData.emit(this.conventionHonorairesData);

    }
  }

  onClose() {
    this.conventionFormClose.emit();
  }

}
