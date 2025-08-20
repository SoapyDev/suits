export interface UserProfile {

  username: string;
  name: string | null;
  birthdate: string | null;
  address: string | null;
  phoneNumber : string | null;
  city: string | null;
  postalCode: string | null;
  province: string | null;
  country: string | null
  profession: string;
  profilePicturePath: string | null;
  validBirthdate: boolean | null;
}
