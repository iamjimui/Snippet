import { Component, Input } from '@angular/core';
import { SnippetSheatModel } from 'src/app/models/snippetSheatModel';

@Component({
  selector: 'app-language-flag',
  templateUrl: './language-flag.component.html',
  styleUrls: ['./language-flag.component.scss']
})
export class LanguageFlagComponent {

  @Input() sheets: SnippetSheatModel[] = [];
  srcTab: string[] = [];

  ngOnInit() {
    this.sheets.forEach(item => {
      if (this.srcTab.indexOf(`assets/images/${item.language.name}.svg`) === -1)
        this.srcTab.push(`assets/images/${item.language.name}.svg`);
    });
  }

}
