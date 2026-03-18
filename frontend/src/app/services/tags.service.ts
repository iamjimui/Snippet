import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { TagModel } from '../models/tags.model';
import { BehaviorSubject, firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TagsService {
  tagsSubject = new BehaviorSubject<string[]>([]);
  tags$ = this.tagsSubject.asObservable();

  tagsSubjectAdmin = new BehaviorSubject<TagModel[]>([]);
  tagsAdmin$ = this.tagsSubjectAdmin.asObservable();

  constructor(private http: HttpClient) { }

  getAll() {
    this.http.get<TagModel[]>(`${environment.apiUrl}/tags`).subscribe({
      next: (tags) => {
        let tagNames: string[] = [];
        tags.forEach((tag) => tagNames.push(tag.name));
        this.tagsSubject.next(tagNames);
        console.log(tags);
      },
    });
  }

  getAllFromAdmin() {
    this.http.get<TagModel[]>(`${environment.apiUrl}/tags`).subscribe({
      next: (tags) => {
        if (tags === null) {
          this.tagsSubjectAdmin = new BehaviorSubject<TagModel[]>([]);
        } else {
          let tagNames: TagModel[] = [];
          tags.forEach((tag) => tagNames.push(tag));
          this.tagsSubjectAdmin.next(tagNames);
          console.log(tags);
        }
      },
    });
  }

  add(name: string, token: string) {
    return new Promise<boolean>(async (resolve, reject) => {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      });
      this.http
        .post<TagModel>(`${environment.apiUrl}/tag`, { name: name }, { headers: headers })
        .subscribe({
          next: (res) => {
            const tab = [...this.tagsSubject.value];
            tab.unshift(res.name);
            this.tagsSubject.next(tab);
            this.getAllFromAdmin();
          },
        });
      resolve(true);
    });
  }

  updateTag(id: string, name: string, token: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.put(`${environment.apiUrl}/tag/` + id, { name: name }, { headers: headers }).subscribe(res => {
      this.getAllFromAdmin();
    })
  }

  deleteTag(id: string, token: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.delete(`${environment.apiUrl}/tag/` + id, { headers: headers }).subscribe(res => {
      this.getAllFromAdmin();
    })
  }
}