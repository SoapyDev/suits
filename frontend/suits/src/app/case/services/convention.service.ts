import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ConventionHonoraires, ConventionHonorairesRequest, ConventionId} from '../models/convention.model';
import {firstValueFrom} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConventionService {

  constructor(private httpClient: HttpClient) {
  }

  async getConventionHonoraires(legalCaseId: string) : Promise<ConventionHonoraires|null> {

    try{
      return await firstValueFrom(this.httpClient.get<ConventionHonoraires>(`${environment.backendUrl}/api/convention/get/${legalCaseId}
      `, {withCredentials: true}));
    } catch(error){
      console.error("Erreur lors de la récupération de la convention d'honoraires : ", error);
      return null;
    }

  }

  async createConventionHonoraires(conventionHonorairesRequest: ConventionHonorairesRequest) : Promise<ConventionHonoraires|null>{
    try {
      console.log("test loop");
      return await firstValueFrom(this.httpClient.put<ConventionHonoraires>(
        `${environment.backendUrl}/api/convention/create`,
        conventionHonorairesRequest, {withCredentials: true}));
    } catch(error) {
      console.error("Erreur lors de la création de la convention d'honoraires : ", error);
      return null;
    }

  }

  async acceptConventionHonoraires(conventionId: ConventionId) : Promise<ConventionHonoraires|null> {
    try {
      return await firstValueFrom(this.httpClient.post<ConventionHonoraires>(
        `${environment.backendUrl}/api/convention/accept`,
        conventionId, {withCredentials: true}));
    } catch (error) {
      console.error("Erreur lors de l'acceptation de la convention d'honoraires : ", error);
      return null;
    }


  }

  async modifyConventionHonoraire(conventionHonorairesRequest: ConventionHonorairesRequest): Promise<ConventionHonoraires|null> {
    try {
      return await firstValueFrom(this.httpClient.put<ConventionHonoraires>(
        `${environment.backendUrl}/api/convention/modify`,
        conventionHonorairesRequest, {withCredentials: true}));
    } catch (error) {
      console.error("Erreur lors de la modification de la convention d'honoraires : ", error);
      return null;
    }
  }


}
