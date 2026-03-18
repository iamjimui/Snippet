import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom, map } from 'rxjs';
import { Menu } from 'src/app/models/snippet.model';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { LanguagesService } from 'src/app/services/languages.service';
import { SnippetsService } from 'src/app/services/snippets.service';
import { TagsService } from 'src/app/services/tags.service';

@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent implements OnInit {

  searchedLanguage: string = "";
  searchedTag: string = "";


  selected = {
    "menu": Menu.ALL_SNIPPET,
    "language": "",
    "tags": [] as Array<string>,
  };

  menu = [
    {
      "title": "All snippets",
      "icon": "list",
      "value": Menu.ALL_SNIPPET,
      "observe": this.snippetService.snippets$
    },
    {
      "title": "My snippets",
      "icon": "finger-print",
      "value": Menu.MY_SNIPPET,
      "observe": this.snippetService.mySnippets$
    },
    {
      "title": "Favorite snippets",
      "icon": "heart",
      "value": Menu.FAV_SNIPPET,
      "observe": this.snippetService.myFavSnippet$
    },
  ];

  constructor(public router: Router, public languagesService: LanguagesService, public tagService: TagsService, private snippetService: SnippetsService) { }

  ngOnInit(): void {
    this.selectMenuItem(Menu.ALL_SNIPPET);
  }

  selectMenuItem(menu: Menu) {
    this.selected.menu = menu;
    this.selected.language = "";
    this.snippetService.setSnippetsView(menu);
  }

  selectLanguage(item: any) {
    console.log(item);
    if (item === '') {
      this.selected.menu = Menu.ALL_SNIPPET;
      this.selected.language = '';
      this.snippetService.setSnippetsView(Menu.ALL_SNIPPET);
      return;
    }
    this.selected.language = item;
    this.selected.menu = Menu.ALL_SNIPPET;
    this.snippetService.filterByLanguage(item);
  }

  removeTag(item: string) {
    let index = this.selected.tags.indexOf(item);
    this.selected.tags.splice(index, 1);
    this.snippetService.filterByTags(this.selected.tags);
  }

  selectTag(tag: string) {
    if (this.selected.tags.indexOf(tag) != -1)
      return;

    this.selected.tags.push(tag);
    this.snippetService.filterByTags(this.selected.tags);
  }

  languageIcon(name: string): string {
    return `assets/images/${name}.svg`;
  }


}
