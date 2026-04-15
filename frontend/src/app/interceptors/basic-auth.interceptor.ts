import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';

export const basicAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  if (req.headers.has('Authorization')) {
    return next(req);
  }

  const storedAuthorization = authService.getAuthorizationHeader();

  if (!storedAuthorization || !req.url.includes('/api/v1/')) {
    return next(req);
  }

  return next(
    req.clone({
      setHeaders: {
        Authorization: storedAuthorization,
      },
    }),
  );
};
