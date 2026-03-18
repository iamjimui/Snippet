import { Component, Input, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { SnippetModel } from "src/app/models/snippet.model";

@Component({
    selector: "app-snippet-card",
    templateUrl: "./snippet-card.component.html",
    styleUrls: ["./snippet-card.component.scss"]
})
export class SnippetCardComponent implements OnInit {
    @Input() snippet?: SnippetModel;

    snippetLink?: string;

    ngOnInit(): void {
        this.snippetLink = `/snippet/${this.snippet!.id!}`;
    }

    constructor(private router: Router) { }

    open(event: Event) {
        let x = event.target as HTMLElement;
        if (x.nodeName === "ION-ICON")
            return;
        this.router.navigate([`/snippet/${this.snippet!.id!}`]);
    }
}
