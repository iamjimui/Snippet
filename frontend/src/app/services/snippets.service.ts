import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, map } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CommentModel, Menu, SnippetModel } from '../models/snippet.model';
import { User } from '../models/user.model';
import { TagModel } from '../models/tags.model';
import { LanguageModel } from '../models/languages.model';
import { ROLE } from '../models/role';

@Injectable({
  providedIn: 'root',
})
export class SnippetsService {
  user: User = User.getInstance();

  private snippetsSubject = new BehaviorSubject<SnippetModel[]>([]);
  snippets$ = this.snippetsSubject.asObservable();

  private mySnippetsSubject = new BehaviorSubject<SnippetModel[]>([]);
  mySnippets$ = this.mySnippetsSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(true);
  loading$ = this.loadingSubject.asObservable();

  private searchedValueSubject = new BehaviorSubject<string>('');
  searchedValue$ = this.searchedValueSubject.asObservable();

  myFavSnippet$ = this.snippets$.pipe(
    map((snippet) =>
      snippet.filter((item) => item.bookmarkedBy.includes(this.user.username!))
    )
  );
  currentView$ = this.snippets$;

  constructor(private http: HttpClient) {
    this.user.id = parseInt(localStorage.getItem('id')!);
  }

  getAll() {
    this.loadingSubject.next(true);
    this.http.get(`${environment.apiUrl}/snippets`).subscribe({
      next: (res: any) => {
        let tab: SnippetModel[] = [];
        res.forEach((element: any) => {
          if (element.visible === true || this.user.role === ROLE.ROLE_ADMIN) {
            let snippet = {} as SnippetModel;
            snippet.tags = [];
            snippet.bookmarkedBy = [];
            snippet.sheets = [];

            snippet.title = element.title;
            snippet.id = element.id;
            snippet.nbComments = element.comments.length;
            snippet.userEmail = element.user.username;
            snippet.createdAt = element.createdDate;
            snippet.visible = !element.visible;

            element.tags.forEach((tag: any) => snippet.tags.push(tag.name));
            element.favorites.forEach((fav: any) => {
              snippet.bookmarkedBy.push(fav.user.username);
            })

            element.sheets.forEach((sheet: any) => {
              snippet.sheets.push({
                id: sheet.id,
                language: sheet.language,
                content: sheet.content,
                name: sheet.name,
              })
            });

            tab.push(snippet);
          }

        });
        this.snippetsSubject.next(tab);
        console.log(tab);
      },
      complete: () => this.loadingSubject.next(false),
    });
  }

  getMySnippet() {
    this.http
      .get<SnippetModel[]>(`${environment.apiUrl}/snippets/me`)
      .subscribe({
        next: (res) => {
          let tab: SnippetModel[] = [];
          res.forEach((element: any) => {
            let snippet = {} as SnippetModel;
            snippet.tags = [];
            snippet.bookmarkedBy = [];
            snippet.sheets = [];

            snippet.title = element.title;
            snippet.id = element.id;
            snippet.nbComments = element.comments.length;
            snippet.userEmail = element.user.username;
            snippet.createdAt = element.createdDate;
            snippet.visible = !element.visible;

            element.tags.forEach((tag: any) => snippet.tags.push(tag.name));
            element.favorites.forEach((fav: any) => {
              snippet.bookmarkedBy.push(fav.user.username);
            })

            element.sheets.forEach((sheet: any) => {
              snippet.sheets.push({
                id: sheet.id,
                language: sheet.language,
                content: sheet.content,
                name: sheet.name,
              })
            });

            tab.push(snippet);


          });
          this.mySnippetsSubject.next(tab);
          console.log(tab);
        }
      });
  }

  setSnippetsView(menu: Menu) {
    switch (menu) {
      case Menu.ALL_SNIPPET:
        this.currentView$ = this.snippets$;
        break;
      case Menu.MY_SNIPPET:
        this.currentView$ = this.mySnippets$;
        break;
      case Menu.FAV_SNIPPET:
        this.currentView$ = this.myFavSnippet$;
        break;
    }
  }

  filterByLanguage(lang: string) {
    this.currentView$ = this.snippets$.pipe(
      map((snippets) =>
        snippets.filter((snippet) =>
          snippet.sheets.some((item) => item.language.name === lang)
        )
      )
    );
  }

  filterTheTags(snippet: SnippetModel, tags: string[]) {
    let found_tag = false;
    snippet.tags.forEach((snippet_tag) => {
      tags.forEach((tag) => {
        if (snippet_tag === tag) {
          found_tag = true;
        }
      })
    });
    return found_tag;
  }

  filterByTags(tags: string[]) {
    if (tags.length === 0) {
      this.getAll();
      this.currentView$ = this.snippets$;
    } else {
      this.currentView$ = this.currentView$.pipe(
        map((snippets) =>
          snippets.filter((item) => this.filterTheTags(item, tags))
        )
      );
    }
  }

  searchingSnippets(value: string) {
    this.searchedValueSubject.next(value);
  }

  add(snippet: SnippetModel) {
    return new Promise((resolve, reject) => {
      this.http
        .post<SnippetModel>(`${environment.apiUrl}/snippet`, {
          user: {
            id: this.user.id
          },
          title: snippet.title,
          visible: !snippet.visible
        })
        .subscribe({
          next: (res) => {
            this.http.get(`${environment.apiUrl}/tags`).subscribe((tags: any) => {
              tags.forEach(async (element: any) => {
                if (snippet.tags.indexOf(element.name) != -1) {
                  this.http.post(`${environment.apiUrl}/snippetTag`, {
                    snippet_id: res.id,
                    tag_id: element.id,
                  }).subscribe(e => {

                  });
                }
              });
            });
            this.http.get(`${environment.apiUrl}/languages`).subscribe((languages: any) => {
              snippet.sheets.forEach(item => {
                let languageObj = languages.find((language: any) => language.name === item.language.name);
                this.http.post(`${environment.apiUrl}/sheet`, {
                  content: item.content,
                  language: {
                    id: languageObj.id
                  },
                  snippet: {
                    id: res.id
                  },
                  name: item.name
                }).subscribe(e => { })
              });
            });
            resolve(true);
          },
          error: (_) => { },
        });
      this.getAll();
      this.getMySnippet();
      this.myFavSnippet$ = this.snippets$.pipe(
        map((snippet) =>
          snippet.filter((item) => item.bookmarkedBy.includes(this.user.username!))
        )
      );
    });
  }

  update(snippet: SnippetModel) {
    return new Promise((resolve, reject) => {
      this.http
        .put<SnippetModel>(
          `${environment.apiUrl}/snippets/${snippet.id}`, {
          user: {
            id: this.user.id
          },
          title: snippet.title,
          visible: !snippet.visible
        }
        )
        .subscribe({
          next: (res) => {
            this.http.delete(`${environment.apiUrl}/snippetTag/${snippet.id}`).subscribe(e => {

            });

            this.http.get(`${environment.apiUrl}/tags`).subscribe((tags: any) => {
              tags.forEach(async (element: any) => {
                if (snippet.tags.indexOf(element.name) != -1) {
                  this.http.post(`${environment.apiUrl}/snippetTag`, {
                    snippet_id: res.id,
                    tag_id: element.id,
                  }).subscribe(e => {

                  });
                }
              });
            });

            this.http.get(`${environment.apiUrl}/languages`).subscribe((languages: any) => {
              snippet.sheets.forEach(item => {
                let languageObj = languages.find((language: any) => language.name === item.language.name);
                console.log(languageObj);
                this.http.post(`${environment.apiUrl}/sheet`, {
                  content: item.content,
                  id: item.id,
                  language: {
                    id: languageObj.id
                  },
                  snippet: {
                    id: res.id
                  },
                  name: item.name
                }).subscribe(e => { })
              });
            });



            this.getAll();
            this.getMySnippet();

            // if (snippet.private) {

            // } else {

            // }

            // const tab = [...this.snippetsSubject.value];
            // let index = tab.findIndex(item => item.id === res.id);
            // tab[index] = res;
            // this.snippetsSubject.next(tab);
            resolve(true);
          },
          error: (e) => { },
        });
    });
  }

  bookmark(snippet: SnippetModel, actions: boolean) {
    if (actions) {
      return new Promise(async (resolve, reject) => {
        this.http
          .post<SnippetModel>(
            `${environment.apiUrl}/favorite`,
            {
              user: {
                id: this.user.id
              },
              snippet: {
                id: snippet.id
              },
              favorite: 1
            }
          )
          .subscribe({
            next: async (res) => {
              // let tab = await firstValueFrom(this.snippets$);
              // let index = tab.findIndex((item) => item.id === snippet.id);
              // tab[index] = res;
              // this.snippetsSubject.next(tab);

              // let tab2 = await firstValueFrom(this.mySnippets$);
              // let index2 = tab2.findIndex((item) => item.id === snippet.id);
              // tab2[index2] = res;
              // this.mySnippetsSubject.next(tab2);
              this.getAll();
              this.getMySnippet();
              resolve(true);
            },
            error: (e) => { },
          });
      });
    }
    return new Promise(async (resolve, reject) => {
      this.http
        .delete(
          `${environment.apiUrl}/favorite/${this.user.id}/${snippet.id}`
        )
        .subscribe({
          next: async (res) => {
            this.getAll();
            this.getMySnippet();
            resolve(true);
          },
          error: (e) => { },
        });
    });

  }

  async getById(id: string) {
    let res: any = await firstValueFrom(
      this.http.get(`${environment.apiUrl}/snippet/${id}`)
    );

    let snippet = {} as SnippetModel;
    snippet.tags = [];
    snippet.bookmarkedBy = [];
    snippet.sheets = [];
    snippet.comments = [];
    snippet.title = res.title;
    snippet.id = res.id;
    snippet.nbComments = res.comments.length;
    snippet.userEmail = res.user.username;
    snippet.createdAt = res.createdDate;
    snippet.visible = !res.visible;
    res.tags.forEach((tag: any) => snippet.tags.push(tag.name));
    console.log(res);
    res.comments.forEach(async (element: any) => {
      snippet.comments.push({
        content: element.message,
        userEmail: element.user.username,
        snippetId: res.id,
        createdAt: element.createdDate,
        updatedAt: element.updatedDate,
        id: element.id
      })
    });
    console.log(snippet.comments);
    res.sheets.forEach((sheet: any) => {
      snippet.sheets.push({
        id: sheet.id,
        language: sheet.language,
        content: sheet.content,
        name: sheet.name,
      })
    });

    return snippet;
  }

  deleteSnippet(snippet: SnippetModel) {
    return new Promise((resolve, reject) => {
      this.http
        .delete(`${environment.apiUrl}/snippet/${snippet.id}`)
        .subscribe({
          next: () => {
            // if (snippet.private) {
            //   const tab = [...this.mySnippetsSubject.value];
            //   const index = tab.findIndex((item) => item.id === snippet.id);
            //   tab.splice(index, 1);
            //   this.mySnippetsSubject.next(tab);
            // } else {
            //   const tab = [...this.snippetsSubject.value];
            //   const index = tab.findIndex((item) => item.id === snippet.id);
            //   tab.splice(index, 1);
            //   this.snippetsSubject.next(tab);

            //   const tab2 = [...this.mySnippetsSubject.value];
            //   const index2 = tab2.findIndex((item) => item.id === snippet.id);
            //   tab2.splice(index2, 1);
            //   this.mySnippetsSubject.next(tab2);
            // }
            resolve(true);
          },
        });
    });
  }

  AddComment(snippetId: string, content: string) {
    return new Promise<CommentModel>((resolve, _) => {
      this.http.post(`${environment.apiUrl}/comment`, {
        message: content,
        user: {
          id: this.user.id
        },
        snippet: {
          id: snippetId
        }
      }).subscribe((res: any) => {
        let comment = {} as CommentModel;
        comment.content = res.message;
        comment.userEmail = this.user.username;
        comment.snippetId = snippetId;
        comment.createdAt = res.createdDate;
        comment.updatedAt = res.updatedDate;
        comment.id = res.id;
        resolve(comment);
      });
    });
  }

  getAllComments(snippetId: string) {
    return new Promise<CommentModel[]>(async (resolve, reject) => {
      let snip = await this.getById(snippetId);
      resolve(snip.comments)
    });
  }

  deleteComment(snippetId: string, commentId: string) {
    return new Promise<boolean>((resolve, reject) => {
      this.http
        .delete(`${environment.apiUrl}/comment/${commentId}`)
        .subscribe((res) => {
          resolve(true);
        });
    });
  }
}
