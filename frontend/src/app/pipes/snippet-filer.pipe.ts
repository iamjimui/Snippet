import { Pipe, PipeTransform } from '@angular/core';
import { SnippetModel } from '../models/snippet.model';

@Pipe({
  name: 'snippetFiler'
})
export class SnippetFilerPipe implements PipeTransform {

  transform(tab: SnippetModel[], searchValue: string): SnippetModel[] {
    if (!tab)
      return [];

    if (!searchValue)
      return tab;

    return tab.filter((snippet) => {
      return snippet.title.toLocaleLowerCase().includes(searchValue.toLowerCase()) ||
        snippet.sheets[0].content.toLocaleLowerCase().includes(searchValue.toLowerCase()) ||
        snippet.userEmail.toLocaleLowerCase().includes(searchValue.toLowerCase()) ||
        snippet.tags.join().toLocaleLowerCase().includes(searchValue.toLowerCase());
    });
  }

}
