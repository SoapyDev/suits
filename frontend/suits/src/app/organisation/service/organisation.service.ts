import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {environment} from '../../../environments/environment.development';
import {Organisation} from '../../home/model/organisation.model';

@Injectable({
  providedIn: 'root'
})
export class OrganisationService {

  private organisation: Organisation | undefined;

  constructor(private httpClient: HttpClient) {
  }

  async getOrganisation() {

    try {
      this.organisation = await firstValueFrom(
        this.httpClient.get<Organisation>(`${environment.backendUrl}/api/organisation`, {
          withCredentials: true }
        )
      );
      return this.organisation;

    }catch (error){
      console.log("Erreur lors de la récupèration de l'organisation");
      return this.getEmptyOrganisation();
    }

  }

  async postOrganisation(organisation: Organisation){

    try {
    await firstValueFrom(
      this.httpClient.put<Organisation>(`${environment.backendUrl}/api/organisation`,
        organisation,
        { withCredentials: true })
    );
    return true;

    }catch (error){
      console.log("Erreur lors de l'enregistrement de l'organisation.");
      return false;
    }
  }

  private getEmptyOrganisation(): Organisation {
    return {
      id: 0,
      name: '',
      shortDescription: '',
      longDescription: '',
      neq: '',
      taxGSTId: '',
      taxQSTId: '',
      address: '',
      city: '',
      country: '',
      postalCode: '',
      province: '',
      phone: '',
      email: '',
      teamMembers: []
    };
  }
}
