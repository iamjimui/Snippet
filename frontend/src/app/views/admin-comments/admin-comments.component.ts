import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommentsModel } from 'src/app/models/comments.model';
import { CommentsService } from 'src/app/services/comments.service';

@Component({
  selector: 'app-admin-comments',
  templateUrl: './admin-comments.component.html',
  styleUrls: ['./admin-comments.component.scss']
})
export class AdminCommentsComponent {
  id = "";
  constructor(
    public commentsService: CommentsService,
    private route: ActivatedRoute
  ) {}

  async ngAfterViewInit() {
    this.id = this.route.snapshot.paramMap.get("id")!;
    await this.commentsService.getByIdFromAdmin(this.id);
  }

  submitForm(event: any) {
    event.preventDefault();
    if (event.submitter.name === "update") {
      var updatedComment = {
        id: event.target.id.value,
        message: event.target.message.value
      }
      this.commentsService.updateComment(this.id, updatedComment, localStorage.getItem("token")!);
    } else if (event.submitter.name === "delete") {
      var updatedComment = {
        id: event.target.id.value,
        message: event.target.message.value
      }
      this.commentsService.deleteComment(this.id, updatedComment, localStorage.getItem("token")!);
    }
  }
}
