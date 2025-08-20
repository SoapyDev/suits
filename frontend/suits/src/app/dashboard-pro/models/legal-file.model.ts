import {UserProfile} from './user-profile.model';

export interface LegalFile {

  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  civicNumber: string;
  streetName: string;
  city: string;
  province: string;
  postalCode: string;
  birthDate: string;
  startOfMandate: string;
  endOfMandate: string;
  isClosed: boolean;
  lawField: string;
  agreement: string;
  legalFileTeamMembers: UserProfile[]

}
