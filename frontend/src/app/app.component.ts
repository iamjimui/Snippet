import { Component } from '@angular/core';
import { SnippetsService } from './services/snippets.service';
import { AuthService } from './services/auth.service';
import { LanguagesService } from './services/languages.service';
import { TagsService } from './services/tags.service';
import { AceEditorService } from './services/ace-editor.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title(title: any) {
    throw new Error('Method not implemented.');
  }

  constructor() { }

}
