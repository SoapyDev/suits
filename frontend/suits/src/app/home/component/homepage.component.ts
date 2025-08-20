import {Component, signal, WritableSignal} from '@angular/core';
import {OrganisationService} from '../../organisation/service/organisation.service';
import {Organisation} from '../model/organisation.model';
import {MatCard, MatCardContent} from '@angular/material/card';
import {SiteNavComponent} from '../../site-nav/site-nav.component';
import {NgOptimizedImage} from '@angular/common';
import { environment } from '../../../environments/environment.development';
import {DomSanitizer, SafeResourceUrl, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-homepage',
  imports: [MatCard, MatCardContent, SiteNavComponent, NgOptimizedImage],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss',
})
export class HomepageComponent {

  protected organisation: Organisation = {
    id: 0,
    name: "",
    shortDescription: "",
    longDescription: "",
    neq: "",
    taxGSTId: "",
    taxQSTId: "",
    address: "",
    city: "",
    country: "",
    postalCode: "",
    province: "",
    phone: "",
    email: "",
    teamMembers: [],
  };

  protected API_KEY: string;
  protected mapUrl: SafeResourceUrl | undefined;

  constructor(private organisationService: OrganisationService, private sanitizer: DomSanitizer) {
    this.API_KEY = environment.google_api_key;
  }

  async ngOnInit(): Promise<void> {
    await this.fetchOrganisation();
  }
  private async fetchOrganisation() {
    this.organisation = await this.organisationService.getOrganisation();
    const orgAddress = this.organisation.address.replace(" ", "+");
    const orgCity = this.organisation.city.replace(" ", "+");
    const orgPostalCode = this.organisation.postalCode.replace(" ", "+");

    const url = `https://www.google.com/maps/embed/v1/place?q=${orgAddress},${orgCity},${orgPostalCode}&key=${this.API_KEY}`;
    this.mapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);

  }
}
