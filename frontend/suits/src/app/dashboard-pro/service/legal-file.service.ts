import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LegalFile} from '../models/legal-file.model';
import {environment} from '../../../environments/environment';
import {firstValueFrom} from 'rxjs';
import {LegalFileRequest} from '../models/legal-file-request.model';


@Injectable({
  providedIn: 'root'
})
export class LegalFileService {

  constructor(private httpClient: HttpClient) { }

  async createLegalFile(legalFileRequest: LegalFileRequest) : Promise<void> {
    await firstValueFrom(this.httpClient.post<LegalFile>(`${environment.backendUrl}/api/dossier`,
      legalFileRequest,
      {withCredentials: true })
    );
  }

  async getAllProfessionalFiles(): Promise<LegalFile[]|null> {

    return await firstValueFrom(this.httpClient.get<LegalFile[]>
            (`${environment.backendUrl}/api/dossier/professionnel`,
              {withCredentials: true}));
  }

  async getAllClientFiles(): Promise<LegalFile[]|null> {

    return await firstValueFrom(this.httpClient.get<LegalFile[]>
    (`${environment.backendUrl}/api/dossier/client`,
      {withCredentials: true}));
  }

  async getProfessionalFile(dossierId: string): Promise<LegalFile> {

    return await firstValueFrom(this.httpClient.get<LegalFile>
    (`${environment.backendUrl}/api/dossier/professionnel/${dossierId}`,
      {withCredentials: true}));

  }

  async getClientFile(dossierId: string): Promise<LegalFile> {
    return await firstValueFrom(this.httpClient.get<LegalFile>
    (`${environment.backendUrl}/api/dossier/client/${dossierId}`,
      {withCredentials: true}));
  }



}
