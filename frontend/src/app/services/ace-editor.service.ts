import { Injectable } from '@angular/core';
import * as ace from "ace-builds";


@Injectable({
  providedIn: 'root'
})
export class AceEditorService {
  constructor() { }

  init() {
    ace.config.set("fontSize", "13px");
    ace.config.set('basePath', 'https://unpkg.com/ace-builds@1.4.12/src-noconflict');
  }
}
