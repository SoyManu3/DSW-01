import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

export const departamentosAdminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login'], { queryParams: { reason: 'session-expired' } });
  }

  if (authService.isAdmin()) {
    return true;
  }

  return router.createUrlTree(['/departamentos'], { queryParams: { denied: '1' } });
};
