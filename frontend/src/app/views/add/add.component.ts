import { AfterViewInit, Component, ElementRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import * as ace from "ace-builds";
import { firstValueFrom } from "rxjs";
import { AlertType } from "src/app/models/alert.model";
import { LanguageModel } from "src/app/models/languages.model";
import { SnippetModel } from "src/app/models/snippet.model";
import { SnippetSheatModel } from "src/app/models/snippetSheatModel";
import { User } from "src/app/models/user.model";
import { AceEditorService } from "src/app/services/ace-editor.service";
import { AlertService } from "src/app/services/alert.service";
import { LanguagesService } from "src/app/services/languages.service";
import { SnippetsService } from "src/app/services/snippets.service";
import { TagsService } from "src/app/services/tags.service";

@Component({
    selector: "app-add",
    templateUrl: "./add.component.html",
    styleUrls: ["./add.component.scss"]
})
export class AddComponent implements AfterViewInit {
    @ViewChild("code") editor?: ElementRef;
    @ViewChild("sheetName") sheetName?: ElementRef;

    searchedLanguage: string = "";
    selectedTags: string[] = [];

    activedSheatIndex = 0;
    aceEditor?: ace.Ace.Editor;

    snippetSheets: SnippetSheatModel[] = [
        {
            id: "",
            language: {
                id: "",
                name: "python",
                createdDate: "",
                updatedDate: "" 
            },
            content: "",
            name: "Sheet 1"
        }
    ];

    title: string = "";
    visible: boolean = false;
    errorTitle: boolean = false;
    errorTags: boolean = false;
    loading: boolean = false;

    constructor(
        public languagesService: LanguagesService,
        public tagService: TagsService,
        private snippetService: SnippetsService,
        private alertService: AlertService,
        private aceEditorService: AceEditorService,
        private router: Router
    ) { }

    async ngOnInit() { }

    ngAfterViewInit(): void {
        this.aceEditorService.init();
        this.aceEditor = ace.edit(this.editor!.nativeElement);
        this.aceEditor.setTheme("ace/theme/dracula");
        this.aceEditor?.session.setMode(`ace/mode/python`);

        this.aceEditor.session.on("change", () => {
            this.snippetSheets[this.activedSheatIndex].content =
                this.aceEditor!.session.getValue();
        });
    }

    addSheat() {
        this.snippetSheets.push({
            id: "",
            language: {
                id: "",
                name: "python",
                createdDate: "",
                updatedDate: ""
            },
            content: "",
            name: `Sheet ${this.snippetSheets.length + 1}`
        });
        this.activateSheat(this.activedSheatIndex + 1);
    }

    removeSheat(i: number) {
        if (this.snippetSheets.length > 1) {
            this.snippetSheets.splice(i, 1);

            if (i === 0) this.activedSheatIndex = 0;
            else this.activedSheatIndex = i - 1;

            this.aceEditor!.session.setMode(
                `ace/mode/${this.snippetSheets[this.activedSheatIndex].language
                }`
            );
            this.aceEditor!.session.setValue(
                this.snippetSheets[this.activedSheatIndex].content
            );
        }
    }

    activateSheat(i: number) {
        this.snippetSheets[this.activedSheatIndex].content =
            this.aceEditor!.getValue();
        this.activedSheatIndex = i;
        this.aceEditor!.session.setMode(
            this.snippetSheets[this.activedSheatIndex].language.name == "console"
                ? "ace/mode/sh"
                : `ace/mode/${this.snippetSheets[this.activedSheatIndex].language
                }`
        );
        this.aceEditor!.session.setValue(
            this.snippetSheets[this.activedSheatIndex].content
        );
    }

    languageIcon(name: string): string {
        return `assets/images/${name}.svg`;
    }

    selectLanguage(language: string) {
        this.snippetSheets[this.activedSheatIndex].language.name = language;
        this.aceEditor?.session.setMode(`ace/mode/${language}`);
    }

    switchChange(value: boolean) {
        this.visible = value;
    }

    onTagsEmitted(tags: string[]) {
        this.selectedTags = tags;
    }

    onErrorTagsEmitted(errorTags: boolean) {
        this.errorTags = errorTags;
    }

    async saveSnippet() {
        if (!this.title) {
            this.errorTitle = true;
            return;
        }
        this.errorTitle = false;

        if (this.selectedTags.length === 0) {
            this.errorTags = true;
            this.alertService.show(
                AlertType.danger,
                `Please tag your snippet.`
            );
            return;
        }
        this.errorTags = false;

        let overflow = false;
        this.snippetSheets.forEach((sheet) => {
            if (sheet.content.split(/\r\n|\r|\n/).length > 2000) {
                this.alertService.show(
                    AlertType.danger,
                    `One of your sheet has more than 1000 lines.`
                );
                overflow = true;
                return;
            } else if (sheet.content.length == 0) {
                this.alertService.show(AlertType.danger, "A sheet cannot be empty.");
                overflow = true;
                return;
            }
        });
        if (overflow) return;

        this.loading = true;

        let tags = await firstValueFrom(this.tagService.tags$);
        this.selectedTags.forEach(async (selectedTag) => {
            if (!tags.includes(selectedTag))
                await this.tagService.add(selectedTag.toLowerCase(), localStorage.getItem("token")!);
        });

        this.snippetSheets.forEach((sheet, i) => {
            sheet.name = this.sheetName!.nativeElement.querySelectorAll(
                ".sheet-title p span"
            )[i].textContent.replace(/ /g, "");
        });

        let snippet = {} as SnippetModel;
        snippet.title = this.title;
        snippet.tags = this.selectedTags;
        snippet.sheets = this.snippetSheets;
        snippet.visible = this.visible;
        snippet.description = "";
        snippet.userEmail = User.getInstance().email!;
        snippet.bookmarkedBy = [];

        await this.snippetService.add(snippet);

        this.loading = false;
        this.title = "";
        this.selectedTags = [];
        this.activedSheatIndex = 0;
        this.snippetSheets = [
            {
                id: "",
                language: {
                    id: "",
                    name: "python",
                    createdDate: "",
                    updatedDate: ""
                },
                content: "",
                name: "Sheet 1"
            }
        ];
        this.visible = false;
        this.aceEditor!.session.setValue("");

        this.alertService.show(
            AlertType.success,
            "Your snippet has been saved.",
            2000,
            false
        );
        this.router.navigate(["/snippets"]);
    }
}
