import { Component, Input } from '@angular/core';
import { AlertType } from 'src/app/models/alert.model';
import { SnippetModel } from 'src/app/models/snippet.model';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-copy-button',
  templateUrl: './copy-button.component.html',
  styleUrls: ['./copy-button.component.scss']
})
export class CopyButtonComponent {

  @Input() snippet?: SnippetModel;

  constructor(private alertService: AlertService) { }

  copy() {
    navigator.clipboard.writeText(this.snippet!.sheets[0].content);
    this.alertService.show(AlertType.success, "Copied!!!", 2000, false);
  }

}
