import { Pipe, PipeTransform } from '@angular/core';
import { SnippetModel } from '../models/snippet.model';

@Pipe({
  name: 'orderBy'
})
export class OrderByPipe implements PipeTransform {

  transform(value: SnippetModel[], arg: "desc" | "asc" = "desc"): SnippetModel[] {
    if (!value)
      return [];

    if (!arg)
      return value;

    if (arg == "desc")
      return value.sort((a, b) => new Date(b.createdAt!).getTime() - new Date(a.createdAt!).getTime());
    else
      return value.sort((a, b) => new Date(a.createdAt!).getTime() - new Date(b.createdAt!).getTime());
  }

}
