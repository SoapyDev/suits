import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {Case} from '../models/case.model';
import {environment} from '../../../environments/environment.development';
import {CaseHeader} from '../models/case-header.model';
import {TeamMembers} from '../../home/model/team-members.model';

@Injectable({
  providedIn: 'root'
})
export class CaseService {

  constructor(
    private http: HttpClient,
  ) {
  }

  private emptyLegalFile() {
    return {
      agreement: '',
      birthdate: '',
      city: '',
      civicNumber: '',
      description: '',
      email: '',
      firstName: '',
      id: '',
      isClosed: false,
      lastName: '',
      lawField: '',
      phoneNumber: '',
      postalCode: '',
      province: '',
      startOfMandate: new Date(),
      endOfMandate: new Date() ,
      status: '',
      streetName: '',
      legalFileTeamMembers: []
    };
  }

  async updateCase(legalCase: Case): Promise<Case> {

    try {
      return await firstValueFrom(
        this.http.put<Case>(`${environment.backendUrl}/api/dossier/professionnel/${legalCase.id}`,
          legalCase,
          {withCredentials: true}
        )
      );

    }catch (error){
      console.error("Erreur lors de la mise à jour du dossier : ", error);
      return this.emptyLegalFile();
    }
  }

  async getCaseByUser(): Promise<Case[]>  {

    try {
      return await firstValueFrom(
        this.http.get<Case[]>(`${environment.backendUrl}/api/dossier/professionnel`, {
          withCredentials: true }
        )
      );

    }catch (error){
      console.error("Erreur lors de la récupération des dossiers : ", error);
      return [];
    }
  }

  async getCasesHeadersByUser():Promise<CaseHeader[]> {

    let casesHeaders: CaseHeader[]=[];
    let cases = await this.getCaseByUser();

    for (let c of cases){
      let tempHeader: CaseHeader = {id:'', tasksNum: 0 , warning:false};
      tempHeader.id = c.id;
      casesHeaders.push(tempHeader)
    }
    casesHeaders.sort((a, b) => a.id.localeCompare(b.id));
    return casesHeaders
  }

  async getCaseByID(caseId: string): Promise<Case> {

    try{
      return await firstValueFrom(
        this.http.get<Case>(`${environment.backendUrl}/api/dossier/professionnel/${caseId}`,
          {withCredentials: true }
        )
      );
    }catch (error){
      console.error("Erreur lors de la récupération du dossier : ", error );
      return this.emptyLegalFile();
    }
  }

  async createCase(legalCase: Case): Promise<Case> {
    try{
      return await firstValueFrom(
        this.http.post<Case>(`${environment.backendUrl}/api/dossier`,
          legalCase,
          {withCredentials: true }
        )
      );
    }catch (error){
      console.error("Erreur lors de la création du dossier : ", error );
      return this.emptyLegalFile();
    }

  }


  async updateCaseTeamMembers(legalCaseId:string, teamMembers: TeamMembers[]): Promise<Case> {
  console.log("teamMembers : ",teamMembers)
    try {
      return await firstValueFrom(
        this.http.put<Case>(`${environment.backendUrl}/api/dossier/${legalCaseId}/teammembers`,
          teamMembers,
          {withCredentials: true}
        )
      );

    }catch (error){
      console.error("Erreur lors de la mise à jour du dossier : ", error);
      return this.emptyLegalFile();
    }
  }
}
