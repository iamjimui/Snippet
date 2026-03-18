import { Component, OnInit } from '@angular/core';
import { AceEditorService } from 'src/app/services/ace-editor.service';
import { LanguagesService } from 'src/app/services/languages.service';
import { SnippetsService } from 'src/app/services/snippets.service';
import { TagsService } from 'src/app/services/tags.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  // Pagination
  snippetsPerView: number = 20;
  start: number = 0;
  end: number = this.snippetsPerView;

  constructor(
    public snippetService: SnippetsService,
    private languagesService: LanguagesService,
    private tagsService: TagsService,
    private aceEditorService: AceEditorService,
  ) { }

  jumpTo(i: number[]) {
    this.start = i[0];
    this.end = i[1];
  }

  ngOnInit(): void {
    this.aceEditorService.init();
    this.snippetService.getMySnippet();
    this.snippetService.getAll();
    this.languagesService.getAll();
    this.tagsService.getAll();
  }

}
