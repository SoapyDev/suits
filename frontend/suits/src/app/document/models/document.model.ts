import {UserProfile} from '../../dashboard-pro/models/user-profile.model';
import {Case} from '../../case/models/case.model';

export interface Document {

  id: number | null,
  name: string | null,
  description: string | null,
  storagePath: string | null,
  signedUrl: string | null,
  fileSize: number | null,
  contentType: string | null,
  uploadedDate: string | null,

  uploadedBy: UserProfile | null,
  legalFile: Case | null

}
