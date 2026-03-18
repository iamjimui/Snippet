import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommentsModel } from '../models/comments.model';
import { environment } from 'src/environments/environment';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {
  commentsSubjectAdmin = new BehaviorSubject<CommentsModel[]>([]);
  commentsAdmin$ = this.commentsSubjectAdmin.asObservable();

  constructor(private http: HttpClient) { }

  getAllFromAdmin() {
    this.http.get<CommentsModel[]>(`${environment.apiUrl}/comments`).subscribe({
      next: (comments) => {
        if(comments === null) {
          this.commentsSubjectAdmin = new BehaviorSubject<CommentsModel[]>([]);
        } else {
          let commentNames: CommentsModel[] = [];
          comments.forEach((comment) => commentNames.push(comment));
          this.commentsSubjectAdmin.next(commentNames);
        }
      },
    });
  }

  getByIdFromAdmin(id: string) {
    this.http.get<CommentsModel[]>(`${environment.apiUrl}/comments/${id}`).subscribe({
      next: (comments) => {
        console.log(comments);
        if(comments === null) {
          this.commentsSubjectAdmin = new BehaviorSubject<CommentsModel[]>([]);
        } else {
          let commentNames: CommentsModel[] = [];
          comments.forEach((comment) => commentNames.push(comment));
          this.commentsSubjectAdmin.next(commentNames);
        }
      },
    });
  }

  updateComment(id: string, updatedComment: any, token: string) {
    const headers = new HttpHeaders({
      'Content-Type':'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.put(`${environment.apiUrl}/comment/` + updatedComment.id, updatedComment, {headers: headers}).subscribe(res => {
      this.getByIdFromAdmin(id);
    })
  }

  deleteComment(id: string,updatedComment: any, token: string) {
    const headers = new HttpHeaders({
      'Content-Type':'application/json',
      'Authorization': 'Bearer ' + token
    });
    return this.http.delete(`${environment.apiUrl}/comment/` + updatedComment.id, {headers: headers}).subscribe(res => {
      this.getByIdFromAdmin(id);
    })
  }
}
