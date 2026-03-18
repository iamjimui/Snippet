import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { UserModel } from '../models/users.model';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class UsersService {
  usersSubjectAdmin = new BehaviorSubject<UserModel[]>([]);
  usersAdmin$ = this.usersSubjectAdmin.asObservable();
  
  meSubjectAdmin = new BehaviorSubject<Object>([]);
  meAdmin$ = this.meSubjectAdmin.asObservable();

  constructor(private http: HttpClient) { }

  getMe(token: string) {
    const headers = new HttpHeaders({
      'Content-Type':'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.get(`${environment.apiUrl}/me`, {headers: headers}).subscribe(res => {
      let userNames: Object = res;
      this.meSubjectAdmin.next(userNames);
    })
  }

  getAllFromAdmin() {
    this.http.get<UserModel[]>(`${environment.apiUrl}/user`).subscribe({
      next: (users) => {
        if(users === null) {
          this.usersSubjectAdmin = new BehaviorSubject<UserModel[]>([]);
        } else {
          let userNames: UserModel[] = [];
          users.forEach((user) => userNames.push(user));
          this.usersSubjectAdmin.next(userNames);
        }
      },
    });
  }

  updateUser(id: string, updatedUser: any, token:string) {
    const headers = new HttpHeaders({
      'Content-Type':'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.put(`${environment.apiUrl}/user/` + id, JSON.stringify(updatedUser), {headers: headers}).subscribe(res => {
      this.getAllFromAdmin();
    })
  }

  deleteUser(id: string, token: string) {
    const headers = new HttpHeaders({
      'Content-Type':'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.delete(`${environment.apiUrl}/user/` + id, {headers: headers}).subscribe(res => {
      this.getAllFromAdmin();
    })
  }
}
