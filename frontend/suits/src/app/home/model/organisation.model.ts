import {TeamMembers} from './team-members.model';

export interface Organisation {
  readonly id: number;
  readonly name: string;
  readonly shortDescription: string;
  readonly longDescription: string;
  readonly neq: string;
  readonly taxGSTId: string;
  readonly taxQSTId: string;
  readonly address: string;
  readonly city: string;
  readonly country: string;
  readonly postalCode: string;
  readonly province: string;
  readonly phone: string;
  readonly email: string;
  readonly teamMembers: TeamMembers[];
}
