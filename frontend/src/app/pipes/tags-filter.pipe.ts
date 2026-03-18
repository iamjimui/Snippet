import { Pipe, PipeTransform } from '@angular/core';
import { TagModel } from '../models/tags.model';

@Pipe({
  name: 'tagsFilter'
})
export class TagsFilterPipe implements PipeTransform {

  transform(tab: string[], searchValue: string): string[] {

    if (!tab)
      return [];

    if (!searchValue)
      return tab;

    return tab.filter((tag) => tag.toLocaleLowerCase().includes(searchValue.toLowerCase()));
  }

}
