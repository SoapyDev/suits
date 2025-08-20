import {LegalFile} from '../../dashboard-pro/models/legal-file.model';
import {Case} from './case.model';

export interface ConventionHonorairesRequest {

  legalFileId: string;
  agreement: string;
  mandat: string;
  tarifHoraire: number;
  estimationPrix: number;
}

export interface ConventionHonoraires {

  id: number;
  legalFile: Case;
  agreement: string;
  dateEntente: Date;
  mandat: string;
  tarifHoraire: number;
  estimationPrix: number;
  creator: string;
  isApproved: boolean;
  approvalDate: Date;

}

export interface ConventionId {
  id: number;
}
