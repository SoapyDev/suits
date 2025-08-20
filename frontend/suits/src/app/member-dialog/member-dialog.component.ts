import {Component, inject, model, signal, WritableSignal} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef
} from "@angular/material/dialog";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatOption, MatSelect} from '@angular/material/select';
import {MemberDialogService} from './service/member-dialog.service';
import {UserProfile} from '../dashboard-pro/models/user-profile.model';

@Component({
  selector: 'app-member-dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatFormField,
    MatLabel,
    ReactiveFormsModule,
    FormsModule,
    MatSelect,
    MatOption
  ],
  templateUrl: './member-dialog.component.html',
  styleUrl: './member-dialog.component.scss'
})
export class MemberDialogComponent {
  readonly dialogRef = inject(MatDialogRef<MemberDialogComponent>);
  readonly data = inject<UserProfile[]>(MAT_DIALOG_DATA);
  readonly memberDialogService = inject(MemberDialogService);

  onNoClick(): void{
    this.dialogRef.close();
  }

  readonly usedMembers = model(this.data);

  protected members : WritableSignal<UserProfile[]> = signal([]);

  protected selectedMember: WritableSignal<UserProfile> = signal(
    {
      username: '',
      name: '',
      birthdate: '',
      address: '',
      phoneNumber: '',
      city: '',
      postalCode: '',
      province: '',
      country: '',
      profession: '',
      profilePicturePath: '',
      validBirthdate: true
    });

  async ngOnInit() {
    await this.loadMembers();
  }

  async loadMembers() {
    try {
      const members = await this.memberDialogService.getAllUsers();
      if (members) {
        this.members.set(members);
      } else {
        console.error('Aucun membre trouvé.');
      }
    } catch (error) {
      console.error("Problème lors de la récupération des membres.", error);
    }
  }

  getMembers(): UserProfile[] {
    let members = this.members();
    for (const usedMember of this.usedMembers()) {
      members = members.filter((m) => m.username !== usedMember.username)
    }

    return members;
  }

  addMember() {
    if (this.selectedMember().username !== '') {
      const members = this.usedMembers();
      members.push(this.selectedMember());
      this.usedMembers.set(members);
    }
    this.dialogRef.close(this.usedMembers());
  }
}
