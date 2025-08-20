import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { LoginPageComponent } from './login/login-page/login-page.component';

import { routes } from './app.routes';
import {NotFoundPageComponent} from './not-found-page/not-found-page.component';
import {provideHttpClient, withFetch} from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    LoginPageComponent,
    NotFoundPageComponent,
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withFetch())
  ]
};
