import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { environment } from 'src/environments/environment';
import { firstValueFrom } from 'rxjs';
import { AlertService } from './alert.service';
import { AlertType } from '../models/alert.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user = User.getInstance();
  constructor(private http: HttpClient) { }

  async register(username: string, password: string) {
    try {
      await firstValueFrom(this.http.post(`${environment.apiUrl}/register`, {
        "username": username,
        "password": password,
      }));

      await this.login(username, password);
    } catch (e: any) {
      throw e;
    }

  }

  async login(username: string, password: string) {

    try {
      let payload: any = await firstValueFrom(this.http.post(`${environment.apiUrl}/authenticate`, {
        "username": username,
        "password": password,
      }));

      localStorage.setItem('token', payload.token);
      localStorage.setItem('username', username);

      let me: any = await firstValueFrom(this.http.get(`${environment.apiUrl}/me`));


      localStorage.setItem('id', me.id);

      let user = User.getInstance();
      user.username = username;
      user.role = me.role;
      user.id = me.id;
    } catch (e: any) {
      throw e;
    }
  }


  async getUserData() {
  }
}
