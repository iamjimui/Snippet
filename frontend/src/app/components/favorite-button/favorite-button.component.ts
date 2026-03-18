import { AfterViewInit, Component, Input } from '@angular/core';
import { AlertType } from 'src/app/models/alert.model';
import { SnippetModel } from 'src/app/models/snippet.model';
import { User } from 'src/app/models/user.model';
import { AlertService } from 'src/app/services/alert.service';
import { SnippetsService } from 'src/app/services/snippets.service';

@Component({
  selector: 'app-favorite-button',
  templateUrl: './favorite-button.component.html',
  styleUrls: ['./favorite-button.component.scss']
})
export class FavoriteButtonComponent implements AfterViewInit {

  @Input() snippet: SnippetModel = {} as SnippetModel;
  user: User = User.getInstance();

  constructor(private snippetService: SnippetsService, private alertService: AlertService) {
  }

  ngAfterViewInit(): void {

  }

  async like() {
    await this.snippetService.bookmark(this.snippet, true);
    this.alertService.show(AlertType.success, "Snippet added to favorites", 2000, false);
  }

  async unlike() {
    await this.snippetService.bookmark(this.snippet, false);
    this.alertService.show(AlertType.success, "Snippet removed to favorites", 2000, false);
  }

}
