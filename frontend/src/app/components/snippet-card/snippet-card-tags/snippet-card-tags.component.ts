import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-snippet-card-tags',
  templateUrl: './snippet-card-tags.component.html',
  styleUrls: ['./snippet-card-tags.component.scss']
})
export class SnippetCardTagsComponent {

  @Input() tags: string[] = [];

}
