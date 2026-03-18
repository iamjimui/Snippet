import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LanguageModel } from '../models/languages.model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguagesService {

  languageSubject = new BehaviorSubject<string[]>([]);
  language$ = this.languageSubject.asObservable();

  loadingSubject$ = new BehaviorSubject<boolean>(true);
  loading$ = this.loadingSubject$.asObservable();

  languagesSubjectAdmin = new BehaviorSubject<LanguageModel[]>([]);
  languagesAdmin$ = this.languagesSubjectAdmin.asObservable();

  priorityMap = new Map<string, number>([
    ["python", 10],
    ["typescript", 9],
    ["javascript", 8],
    ["yaml", 8],
    ["html", 5],
    ["css", 5],
    ["dockerfile", 6],
    ["json", 7],
  ]);


  constructor(private http: HttpClient) { }



  async getAll() {
    this.http.get<LanguageModel[]>(`${environment.apiUrl}/languages`).subscribe({
      next: (languages) => {
        let languagesName: string[] = [];
        languages.forEach(language => {
          language.priority = this.priorityMap.get(language.name);
          language.priority = language.priority ?? 0;
        });
        languages.sort((a, b) => b.priority! - a.priority!);
        languages.forEach(language => languagesName.push(language.name));
        this.languageSubject.next(languagesName);
      },
      complete: () => this.loadingSubject$.next(false),
    });
  }

  getAllFromAdmin() {
    this.http.get<LanguageModel[]>(`${environment.apiUrl}/languages`).subscribe({
      next: (languages) => {
        console.log(languages);
        if (languages === null) {
          this.languagesSubjectAdmin = new BehaviorSubject<LanguageModel[]>([]);
        } else {
          let languagesName: LanguageModel[] = [];
          languages.forEach((language) => languagesName.push(language));
          this.languagesSubjectAdmin.next(languagesName);
          console.log(languages);
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
        .post<LanguageModel>(`${environment.apiUrl}/language`, { name: name }, { headers: headers })
        .subscribe({
          next: (res) => {
            const tab = [...this.languageSubject.value];
            tab.unshift(res.name);
            this.languageSubject.next(tab);
            this.getAllFromAdmin();
          },
        });
      resolve(true);
    });
  }

  updateLanguage(id: string, name: string, token: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.put(`${environment.apiUrl}/language/${id}`, { name: name }, { headers: headers }).subscribe(res => {
      this.getAllFromAdmin();
    })
  }

  deleteLanguage(id: string, token: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.delete(`${environment.apiUrl}/language/` + id, { headers: headers }).subscribe(res => {
      this.getAllFromAdmin();
    })
  }
}
