import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Profile} from './profile.model';
import {firstValueFrom} from 'rxjs';
import {environment} from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) { }

  async getCurrentProfile() : Promise<Profile>{
    try{
      return await firstValueFrom(
        this.http.get<Profile>(
          `${environment.backendUrl}/api/user/profile`,
          {withCredentials: true}
        ),
      );
    }catch (error){
      console.error("Erreur lors de la récupération du profile : ", error);
      return this.defaultProfile();
    }
  }

  async postProfile(profile : Profile) : Promise<Profile>{

    try{
      return await firstValueFrom(
        this.http.post<Profile>(
          `${environment.backendUrl}/api/user/profile`,
          profile,
          {withCredentials: true}
        )
      );
    }catch (error){
      console.error("Erreur lors de la mise à jours du profile : ", error);
      return this.defaultProfile();
    }
  }

  private defaultProfile(): Profile {
    return {
      address: '',
      birthdate: new Date(),
      city: '',
      country: '',
      name: '',
      phoneNumber: '',
      postalCode: '',
      profession: '',
      profilePicturePath: '',
      province: '',
      username: ''
    };
  }
}
