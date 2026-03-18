import { Component, Input } from '@angular/core';
import { SnippetModel } from 'src/app/models/snippet.model';

@Component({
  selector: 'app-snippet-card-code',
  templateUrl: './snippet-card-code.component.html',
  styleUrls: ['./snippet-card-code.component.scss']
})
export class SnippetCardCodeComponent {

  @Input() snippet: SnippetModel = {} as SnippetModel;
}
