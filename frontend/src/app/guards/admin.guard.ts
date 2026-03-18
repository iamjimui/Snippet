import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { environment } from 'src/environments/environment';

export const adminGuard: CanActivateFn = async (route, state) => {
  const http = inject(HttpClient);
  const router = inject(Router);
  let res : any = await firstValueFrom(http.get(`${environment.apiUrl}/me`));
  if (res.role === "ROLE_USER") {
    return router.navigateByUrl('snippets');
  }
  return true;
};
