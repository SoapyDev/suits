import {Component, EventEmitter, Input, Output, SimpleChanges} from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader} from '@angular/material/card';
import {MatError, MatFormField, MatHint, MatSuffix} from '@angular/material/form-field';
import {MatButton, MatFabButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatInput} from '@angular/material/input';
import {MatDivider} from '@angular/material/divider';
import {NgForOf, NgIf} from '@angular/common';
import {Organisation} from '../../home/model/organisation.model';
import {OrganisationService} from '../service/organisation.service';

@Component({
  selector: 'app-organisation',
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
    MatSuffix,
    MatDivider,
    NgIf,
    NgForOf,
    MatButton,
    ReactiveFormsModule
  ],
  templateUrl: './organisation.component.html',
  styleUrl: './organisation.component.scss'
})

export class OrganisationComponent {
  organisationForm!: FormGroup;

  @Input()
  showConfirmationError = false;
  @Input()
  showSetOrganisationConfirmation = false;
  @Input()
  organisation!: Organisation;

  @Output()
  onSubmit = new EventEmitter<Organisation>();

  initialOrganisation!: Organisation;

  parameters: {
    key: keyof Organisation;
    label: string;
    type: 'text' | 'number' | 'email' | 'array';
    required?: boolean;
    maxLength?: number;
    pattern?: string;
    errorMsg?: string;
  }[] = [
    { key: 'id', label: 'ID', type: 'number', required:false },
    { key: 'name', label: 'Nom', type: 'text', required:true, maxLength:100},
    { key: 'shortDescription', label: 'Description courte', type: 'text', required:true},
    { key: 'longDescription', label: 'Description longue', type: 'text', required:true},
    { key: 'neq', label: 'Numéro d’entreprise (NEQ)', type: 'text', required:true, pattern:'^[0-9]{10}|[0-9]{0}$', errorMsg:"Doit être composé de 10 chiffres et ne peut pas être négatif"},
    { key: 'taxGSTId', label: 'Numéro de taxe GST', type: 'text', required:true, maxLength:20, errorMsg:"Doit avoir maximum 20 caractères"},
    { key: 'taxQSTId', label: 'Numéro de taxe QST', type: 'text', required:true, maxLength:20, errorMsg:"Doit avoir maximum 20 caractères"},
    { key: 'address', label: 'Adresse', type: 'text', required:true, pattern:'[a-zA-Z0-9 \\-àâçéèêëôûùüñÀÂÇÉÈÊËÎÏÔÛÙÜ]{0,255}', errorMsg:" Doit avoir maximum 255 caractères alphanumériques" },
    { key: 'city', label: 'Ville', type: 'text', required:true, pattern:'^\\D{0,255}$', errorMsg:" Doit avoir maximum 255 caractères alphabétiques" },
    { key: 'country', label: 'Pays', type: 'text', required:true, maxLength:100, pattern:'^\\D{0,100}$',errorMsg:" Doit avoir maximum 100 caractères alphabétiques" },
    { key: 'postalCode', label: 'Code postal', type: 'text', required:true, maxLength:7, pattern:'^[A-Za-z][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]|[0-9]{0}$',errorMsg:" Le code postal doit respecter le format A1A 1A1" },
    { key: 'province', label: 'Province', type: 'text', required:true, maxLength:20, pattern:'^\\D{0,20}$', errorMsg:"Doit avoir maximum 20 caractères alphabétiques"  },
    { key: 'phone', label: 'Téléphone', type: 'text', required:true, maxLength:12, pattern:'^\\d{3}[-]?\\d{3}[-]?\\d{4}|\\d{0}$', errorMsg:"Le numéro doit respecter le format XXX-XXX-XXXX" },
    { key: 'email', label: 'Courriel', type: 'email', required:true, pattern:'^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$',errorMsg:"Le courriel doit être valide." },
    { key: 'teamMembers', label: 'Membres de l’équipe', type: 'array' },
  ] as const;

  readyToSubmit!: boolean;
  hasAuthority: boolean = localStorage.getItem("role") == "PROFESSIONNEL";

  constructor(private organisationService: OrganisationService,private fb: FormBuilder) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['organisation'] && this.organisation) {
      this.initializeForm();
    }
  }
   async initializeForm(): Promise<void> {


    this.initialOrganisation = JSON.parse(JSON.stringify(this.organisation));

     const formControls: Record<keyof Organisation, any> = {} as Record<keyof Organisation, any>;

     this.parameters.forEach(field => {
       const validators = [];

       if (field.required) {
         validators.push(Validators.required);
       }
       if (field.maxLength) {
         validators.push(Validators.maxLength(field.maxLength));
       }
       if (field.pattern) {
         validators.push(Validators.pattern(field.pattern));
       }

       formControls[field.key] = [this.organisation[field.key] || '', validators];

     });

     formControls['teamMembers'] = this.fb.array([]);

     this.organisationForm = this.fb.group(formControls);

     if (this.organisation?.teamMembers?.length) {
       this.organisation.teamMembers.forEach(member => this.addTeamMember(member));
     }

     this.organisationForm.valueChanges.subscribe(() => {
       this.readyToSubmit = this.organisationForm.valid && !this.areOrganisationsEqual(this.organisationForm.value, this.initialOrganisation);
     });
   }

  isFieldValid(fieldKey: string): boolean {
    const control = this.organisationForm.get(fieldKey);
    return control ? control.invalid && (control.dirty || control.touched) : false;
  }


  getOrganisationValue(fieldKey: keyof Organisation): any {

    const control = this.organisationForm.get(fieldKey) as FormControl;
    return control?.value || '';
  }

  editMode: Partial<Record<keyof Organisation, boolean>> = {};
  editTeam: boolean = false;


  toggleEditMode(fieldKey: keyof Organisation) {
    const control = this.organisationForm.get(fieldKey);

    if (fieldKey !== 'id') {
      if(!this.isEditMode(fieldKey) || control?.valid){

        this.editMode[fieldKey] = !this.editMode[fieldKey];
      }
    }
  }

  isEditMode(fieldKey: keyof Organisation): boolean {
    return !!this.editMode[fieldKey];
  }

  toggleEditTeam() {

      if(!this.editTeam || this.organisationForm.valid){

        this.editTeam = !this.editTeam;
      }
  }

  get teamMembersArray(): FormArray {
    return this.organisationForm.get('teamMembers') as FormArray;
  }

  addTeamMember(member?: { name: string, service: string }) { {

    const teamMemberFormGroup = this.fb.group({
      name: [member?.name || '', [Validators.required]],
      service: [member?.service || '', [Validators.required]],
    });

    this.teamMembersArray.push(teamMemberFormGroup);
  }
  }

  removeTeamMember(index: number): void {
    this.teamMembersArray.removeAt(index);
  }

  areOrganisationsEqual(org1: Organisation, org2: Organisation): boolean {
    return JSON.stringify(org1) === JSON.stringify(org2);
  }

  onSubmitClick() {

    if (this.readyToSubmit && this.organisationForm.valid) {
      this.onSubmit.emit(this.organisationForm.value);
    }
    this.readyToSubmit = false;
  }
}
