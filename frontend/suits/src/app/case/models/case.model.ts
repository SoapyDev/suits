import {TeamMembers} from '../../home/model/team-members.model';
import {UserProfile} from '../../dashboard-pro/models/user-profile.model';

export interface Case {
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

  birthdate: string;
  description: string;
  status: string,
  startOfMandate: Date,
  endOfMandate: Date,
  isClosed: boolean,
  lawField : string,
  agreement: string,
  //tasks : TaskHeader[],
  // documents : CaseDocument[],
  legalFileTeamMembers : UserProfile[],
}
