import { AfterViewInit, Component, ElementRef, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { SnippetModel } from "src/app/models/snippet.model";
import { User } from "src/app/models/user.model";
import { SnippetsService } from "src/app/services/snippets.service";
import * as ace from "ace-builds";
import { AlertService } from "src/app/services/alert.service";
import { AlertType } from "src/app/models/alert.model";
import { AceEditorService } from "src/app/services/ace-editor.service";
import { SnippetSheatModel } from "src/app/models/snippetSheatModel";

@Component({
    selector: "app-details",
    templateUrl: "./details.component.html",
    styleUrls: ["./details.component.scss"]
})
export class DetailsComponent implements AfterViewInit {
    @ViewChild("code") editor?: ElementRef;

    snippet = {
        title: "",
        tags: [] as string[],
        nbComments: 0,
        description: "",
        sheets: [] as SnippetSheatModel[],
        visible: false,
        bookmarkedBy: [] as string[],
        userEmail: "a@a",
        createdAt: "",
        updatedAt: ""
    } as SnippetModel;

    activedSheatIndex = 0;
    aceEditor?: ace.Ace.Editor;
    user: User = User.getInstance();
    loading: boolean = true;

    constructor(
        private route: ActivatedRoute,
        public snippetService: SnippetsService,
        private alertService: AlertService,
        private router: Router,
        private aceEditorService: AceEditorService
    ) { }

    async ngOnInit() {
        console.log(this.user);
        let id = this.route.snapshot.paramMap.get("id");
        this.snippet = await this.snippetService.getById(id!);
        this.activateSheat(0);
    }

    ngAfterViewInit(): void {
        this.aceEditorService.init();
        this.aceEditor = ace.edit(this.editor!.nativeElement);
        this.aceEditor.setTheme("ace/theme/dracula");
        this.aceEditor.setReadOnly(true);
    }

    activateSheat(i: number) {
        // this.snippetSheats[this.activedSheatIndex].content = this.aceEditor!.getValue();
        this.activedSheatIndex = i;
        this.aceEditor!.session.setMode(
            this.snippet.sheets[this.activedSheatIndex].language.name == "console"
                ? "ace/mode/sh"
                : `ace/mode/${this.snippet.sheets[this.activedSheatIndex].language}`
        );
        this.aceEditor!.setValue(this.snippet.sheets[this.activedSheatIndex].content);
    }

    languageIcon(name: string): string {
        return `assets/images/${name}.svg`;
    }

    copyCode() {
        navigator.clipboard.writeText(this.snippet.sheets[this.activedSheatIndex].content);
        this.alertService.show(AlertType.success, "Copied!!!", 2000, false);
    }

    async delete() {
        let ok = confirm("Do you really want to delete this snippet ? ");
        if (!ok) return;

        await this.snippetService.deleteSnippet(this.snippet);
        this.router.navigate(["/snippets"]);
    }

    edit() {
        this.router.navigate([`edit/${this.snippet.id}`]);
    }
}
