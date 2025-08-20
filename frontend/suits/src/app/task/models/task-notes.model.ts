import {UserProfile} from '../../dashboard-pro/models/user-profile.model';

export interface TaskNote {
  id: number,
  creator: UserProfile,
  creationDate: string,
  updateDate: string,
  content: string
}
