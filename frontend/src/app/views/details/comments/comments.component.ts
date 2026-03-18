import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommentModel, SnippetModel } from 'src/app/models/snippet.model';
import { User } from 'src/app/models/user.model';
import { SnippetsService } from 'src/app/services/snippets.service';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent {

  @ViewChild("inputComment") inputComment?: ElementRef;
  @ViewChild("bottom") bottom?: ElementRef;

  id: string | null = "";
  comments: CommentModel[] = [];
  showEmojiPicker: boolean = false;
  comment: string = "";
  loadingComments: boolean = true;
  user: User = User.getInstance();

  constructor(private route: ActivatedRoute, private snippetService: SnippetsService) { }

  async ngOnInit() {
    this.id = this.route.snapshot.paramMap.get("id");



    this.comments = await this.snippetService.getAllComments(this.id!);
    this.comments.sort((a, b) => (new Date(a.createdAt)).getTime() - (new Date(b.createdAt)).getTime());

    this.loadingComments = false;

    this.inputComment?.nativeElement.addEventListener("keypress", (event: any) => {
      if (event.key === "Enter") {
        event.preventDefault();
        this.addComment();
      }
    });

    if (this.bottom) {
      setTimeout(() => {
        this.bottom!.nativeElement.scrollIntoView({ behavior: "smooth" });
      }, 500);
    }

  }


  emojiSelect(event: any) {
    this.comment += event.emoji.native;
    this.inputComment?.nativeElement.focus();
  }

  async addComment() {
    if (this.comment == "")
      return;

    let res = await this.snippetService.AddComment(this.id!, this.comment);
    this.comments.push(res);

    this.comment = "";
    this.showEmojiPicker = false;
    this.bottom!.nativeElement.scrollIntoView({ behavior: "smooth" });
  }

  containsOnlyEmojis(text: string) {
    const onlyEmojis = text.replace(new RegExp('[\u0000-\u1eeff]', 'g'), '')
    const visibleChars = text.replace(new RegExp('[\n\r\s]+|( )+', 'g'), '')
    return onlyEmojis.length === visibleChars.length
  }

  async deleteComment(comment: CommentModel) {
    let ok = confirm("Do you really want to delete this comment ? ");
    if (!ok)
      return;

    await this.snippetService.deleteComment(this.id!, comment.id);
    let index = this.comments.indexOf(comment);
    this.comments.splice(index, 1);
  }

  @HostListener('document:click', ['$event', '$event.target'])
  closeEmojiPicker(event: MouseEvent, targetElement: HTMLElement): void {

    let emojiMart = document.getElementById("emojiMart");
    if (!emojiMart)
      return;

    let clickedInside = emojiMart.contains(targetElement);

    if (targetElement.getAttribute("name") === "happy-outline")
      clickedInside = true;

    if (!clickedInside)
      this.showEmojiPicker = false;

  }

}
