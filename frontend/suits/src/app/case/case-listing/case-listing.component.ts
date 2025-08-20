import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, output} from '@angular/core';
import {MatAnchor, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatDialog} from '@angular/material/dialog';
import {LegalFileDialogComponent} from '../legal-file-dialog/legal-file-dialog.component';
import {LegalFileRequest} from '../../dashboard-pro/models/legal-file-request.model';
import {Case} from '../models/case.model';
import {CaseService} from '../services/case.service';

@Component({
  selector: 'app-case-listing',
  imports: [
    MatAnchor,
    MatIconButton,
    MatIcon,
  ],
  templateUrl: './case-listing.component.html',
  styleUrl: './case-listing.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true,
})
export class CaseListingComponent {

  createdLegalFile = output<LegalFileRequest>();

  protected caseService : CaseService;
  protected cases: Case[] = [];
  readonly dialog = inject(MatDialog);

  protected openNewFileDialog() {
    this.dialog.open(LegalFileDialogComponent, {
      width: '100%',
      maxWidth: '800px',
    }).afterClosed().subscribe(result => {this.createdLegalFile.emit(result);});
  };


  constructor(caseService: CaseService) {
    this.caseService = caseService;
  }

  async ngOnInit() {
    this.cases = await this.caseService.getCaseByUser();
  }

}
