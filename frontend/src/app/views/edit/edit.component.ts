import { Component, ElementRef, ViewChild } from "@angular/core";
import { SnippetSheatModel } from "src/app/models/snippetSheatModel";
import { AlertService } from "src/app/services/alert.service";
import { LanguagesService } from "src/app/services/languages.service";
import { SnippetsService } from "src/app/services/snippets.service";
import { TagsService } from "src/app/services/tags.service";
import * as ace from "ace-builds";
import { ActivatedRoute } from "@angular/router";
import { SnippetModel } from "src/app/models/snippet.model";
import { firstValueFrom, map } from "rxjs";
import { AlertType } from "src/app/models/alert.model";
import { User } from "src/app/models/user.model";
import { Location } from "@angular/common";
import { AceEditorService } from "src/app/services/ace-editor.service";

@Component({
    selector: "app-edit",
    templateUrl: "./edit.component.html",
    styleUrls: ["./edit.component.scss"]
})
export class EditComponent {
    @ViewChild("code") editor?: ElementRef;
    @ViewChild("sheetName") sheetName?: ElementRef;

    searchedLanguage: string = "";
    searchedTag: string = "";
    newTag: string = "";
    activedSheatIndex = 0;
    aceEditor?: ace.Ace.Editor;

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

    selectedTags: string[] = [];
    snippetSheats: SnippetSheatModel[] = [
        {
            id: "",
            language: {
                id: "",
                name: "python",
                createdDate: "",
                updatedDate: ""
            },
            content: "",
            name: ""
        }
    ];

    title: string = "";
    visible: boolean = false;
    errorTitle: boolean = false;
    errorTags: boolean = false;
    loading: boolean = false;

    id = "";

    constructor(
        public languagesService: LanguagesService,
        public tagService: TagsService,
        private route: ActivatedRoute,
        public snippetService: SnippetsService,
        private alertService: AlertService,
        private location: Location,
        private aceEditorService: AceEditorService
    ) {}

    async ngAfterViewInit() {
        this.id = this.route.snapshot.paramMap.get("id")!;

        this.loading = true;
        this.snippet = await this.snippetService.getById(this.id);

        this.title = this.snippet.title;
        this.selectedTags = this.snippet.tags;
        this.snippetSheats = this.snippet.sheets;
        this.visible = this.snippet.visible;

        this.aceEditorService.init();
        this.aceEditor = ace.edit(this.editor!.nativeElement);
        this.aceEditor.setTheme("ace/theme/dracula");
        this.aceEditor?.session.setMode(
            `ace/mode/${this.snippet.sheets[0].language}`
        );

        this.aceEditor!.session.setValue(this.snippet.sheets[0].content);
        this.aceEditor.session.on("change", () => {
            this.snippetSheats[this.activedSheatIndex].content =
                this.aceEditor!.session.getValue();
        });
        this.loading = false;
    }

    selectTag(tag: string) {
        if (this.selectedTags.indexOf(tag) != -1) return;
        this.selectedTags.push(tag);

        this.errorTags = false;
        this.newTag = "";
        this.searchedTag = "";
    }

    removeTag(tag: string) {
        let index = this.selectedTags.indexOf(tag);
        this.selectedTags.splice(index, 1);
    }

    async tagChange() {
        let value = await firstValueFrom(
            this.tagService.tags$.pipe(
                map((tags) => tags.filter((item) => item === this.searchedTag))
            )
        );

        if (!value.length) this.newTag = this.searchedTag;
        else this.newTag = "";
    }

    addSheat() {
        this.snippetSheats.push({
            id: "",
            language: {
                id: "",
                name: "python",
                createdDate: "",
                updatedDate: ""
            },
            content: "",
            name: `sheet ${this.snippet.sheets.length + 1}`
        });
        this.activateSheat(this.activedSheatIndex + 1);
    }

    removeSheat(i: number) {
        if (this.snippetSheats.length > 1) {
            this.snippetSheats.splice(i, 1);
            if (i === 0) this.activedSheatIndex = 0;
            else this.activedSheatIndex = i - 1;
        }
    }

    activateSheat(i: number) {
        this.snippetSheats[this.activedSheatIndex].content =
            this.aceEditor!.getValue();
        this.activedSheatIndex = i;
        this.aceEditor!.session.setMode(
            this.snippetSheats[this.activedSheatIndex].language.name == "console"
                ? "ace/mode/sh"
                : `ace/mode/${
                      this.snippetSheats[this.activedSheatIndex].language
                  }`
        );
        this.aceEditor!.session.setValue(
            this.snippetSheats[this.activedSheatIndex].content
        );
    }

    languageIcon(name: string): string {
        return `assets/images/${name}.svg`;
    }

    selectLanguage(language: string) {
        this.snippetSheats[this.activedSheatIndex].language.name = language;
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

    async editSnippet() {
        if (!this.title) {
            this.errorTitle = true;
            return;
        }
        this.errorTitle = false;

        if (this.selectedTags.length === 0) {
            this.errorTags = true;
            return;
        }
        this.errorTags = false;

        this.loading = true;
        this.selectedTags.forEach(async (selectedTag) => {
            let value = await firstValueFrom(
                this.tagService.tags$.pipe(
                    map((tags) => tags.filter((item) => item === selectedTag))
                )
            );
            if (!value.length) await this.tagService.add(selectedTag, localStorage.getItem("token")!);
        });

        this.snippetSheats.forEach((sheet, i) => {
            sheet.name = this.sheetName!.nativeElement.querySelectorAll(
                ".sheet-title p span"
            )[i].textContent.replace(/ /g, "");
        });

        let snippet = {} as SnippetModel;
        snippet.title = this.title;
        snippet.tags = this.selectedTags;
        snippet.sheets = this.snippetSheats;
        snippet.visible = this.visible;
        snippet.description = this.snippet.description;
        snippet.userEmail = User.getInstance().email!;
        snippet.bookmarkedBy = this.snippet.bookmarkedBy;
        snippet.createdAt = this.snippet.createdAt;
        snippet.updatedAt = this.snippet.updatedAt;
        snippet.nbComments = this.snippet.nbComments;
        snippet.id = this.id;

        await this.snippetService.update(snippet);

        this.loading = false;

        this.location.back();
        this.alertService.show(
            AlertType.success,
            "Your snippet has been edited.",
            2000,
            false
        );
    }
}
