import { CanActivateFn } from '@angular/router';

export const appGuardGuard: CanActivateFn = (route, state) => {
  return true;
};
