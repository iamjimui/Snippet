import { Pipe, PipeTransform } from '@angular/core';
import { LanguageModel } from '../models/languages.model';

@Pipe({
  name: 'languagesFilter'
})
export class LanguagesFilterPipe<T> implements PipeTransform {

  transform(tab: string[], searchValue: string): string[] {

    if (!tab)
      return [];

    if (!searchValue)
      return tab;

    return tab.filter((language) => language.toLocaleLowerCase().includes(searchValue.toLowerCase()));
  }

}
