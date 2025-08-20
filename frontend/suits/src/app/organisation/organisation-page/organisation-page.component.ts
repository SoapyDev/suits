import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, signal} from '@angular/core';
import {SiteNavComponent} from "../../site-nav/site-nav.component";
import {MediaMatcher} from '@angular/cdk/layout';
import {OrganisationComponent} from '../component/organisation.component';
import {HttpErrorResponse} from '@angular/common/http';
import {Organisation} from '../../home/model/organisation.model';
import {OrganisationService} from '../service/organisation.service';
import {SiteNavPickerComponent} from "../../site-nav-picker/site-nav-picker.component";

@Component({
  selector: 'app-organisation-page',
    imports: [
        SiteNavComponent,
        OrganisationComponent,
        SiteNavPickerComponent,
    ],
  templateUrl: './organisation-page.component.html',
  styleUrl: './organisation-page.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class OrganisationPageComponent {


  organisation!: Organisation;
  protected readonly isMobile = signal(true);
  private readonly _mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;
  showConfirmationError: boolean;
  showSetOrganisationConfirmation: boolean;

  constructor(
    private organisationService: OrganisationService
  ) {
    const media = inject(MediaMatcher);
    this._mobileQuery = media.matchMedia('(max-width: 600px)');
    this.isMobile.set(this._mobileQuery.matches);
    this._mobileQueryListener = () => this.isMobile.set(this._mobileQuery.matches);
    this._mobileQuery.addEventListener('change', this._mobileQueryListener);
    this.showConfirmationError = false;
    this.showSetOrganisationConfirmation = false;
  }

  async ngOnInit(): Promise<void> {
    await this.fetchOrganisation();
  }

  private async fetchOrganisation() {

    this.organisation = await this.organisationService.getOrganisation()

  }


  async onSubmit(organisation: Organisation) {

    try{
      await this.organisationService.postOrganisation(organisation);

    } catch(error){
      if(error instanceof HttpErrorResponse){
        if(error.status == 403){
          this.showConfirmationError = true;
          this.showSetOrganisationConfirmation = false;
        }else{
          this.showConfirmationError = false;
          this.showSetOrganisationConfirmation = true;
        }
      }
    }
  }
}
