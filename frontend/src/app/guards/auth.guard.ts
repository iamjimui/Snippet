import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { User } from '../models/user.model';
import { firstValueFrom } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { ROLE } from '../models/role';

export const authGuard: CanActivateFn = async (route, state) => {
  const http = inject(HttpClient);
  const router = inject(Router);

  let user = User.getInstance();

  let token = localStorage.getItem('token');

  if (token) {
    user.username = localStorage.getItem('username') ?? "";
    let res: any = await firstValueFrom(http.get(`${environment.apiUrl}/me`));
    if (res.role === ROLE.ROLE_ADMIN) {
      user.role = ROLE.ROLE_ADMIN;
    }
    return true;
  }

  return router.navigateByUrl('login');
};
