import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {Observable} from 'rxjs';
import {Document} from '../models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {

  constructor( private httpClient: HttpClient) { }

  uploadDocument(legalFileId: String, fileDescription: string, file: File){

    const formData = new FormData();
    formData.append("file", file);
    formData.append("fileDescription", fileDescription);

    return this.httpClient.post(`${environment.backendUrl}/api/dossier/${legalFileId}/documents`,
        formData,
        {
          withCredentials: true,
          reportProgress: true,
          observe: 'events'
        }
      );
  }

  deleteFile(legalFileId: String, documentId: number){

    return this.httpClient.delete(`${environment.backendUrl}/api/dossier/${legalFileId}/documents/${documentId}`,
      {
        withCredentials: true
      }
    );
  }

  getDocuments(legalFileId: string): Observable<Document[]> {
    return this.httpClient.get<Document[]>(
      `${environment.backendUrl}/api/dossier/${legalFileId}/documents`,
      { withCredentials: true }
    );
  }


}
