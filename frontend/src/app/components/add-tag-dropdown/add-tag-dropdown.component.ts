import { Component, Input, Output, EventEmitter } from "@angular/core";
import { TagsService } from "src/app/services/tags.service";
import { firstValueFrom } from "rxjs";
import { AlertService } from "src/app/services/alert.service";
import { AlertType } from "src/app/models/alert.model";
@Component({
    selector: 'add-tag-dropdown',
    templateUrl: './add-tag-dropdown.component.html',
    styleUrls: ['./add-tag-dropdown.component.scss']
})
export class AddTagDropdownComponent {
    @Input() selectedTags: string[] = [];
	@Output() selectedTagsEvent = new EventEmitter<string[]>();
	@Output() errorTagsEvent = new EventEmitter<boolean>();

	searchedTag: string = "";
    newTag: string = "";

	errorTags: boolean = false;

	constructor(
		public tagService: TagsService,
		private alertService: AlertService,
	) {}

	hasSpecialCaracters(string: string) {
        const pattern = /^[A-Za-z0-9\-]+$/;
        return !pattern.test(string);
    }

    selectTag(tag: string, event: any) {
        event.stopPropagation();

        if (this.selectedTags.includes(tag)) return;

        if (this.selectedTags.length > 2) {
            this.alertService.show(AlertType.danger, "You can't add more than 3 tags", 3000);
            return;
        }

        if (tag.trim().length < 2 || tag.includes(" ") || this.hasSpecialCaracters(tag)) {
            this.alertService.show(
                AlertType.danger,
                "Tags must be at least 2-character-long and can only contain letters, numbers and dashes ('-')",
                3000
            );
            return;
        }

        this.selectedTags.push(tag.trim().toLowerCase());
		this.selectedTagsEvent.emit(this.selectedTags);
        this.errorTags = false;
		this.errorTagsEvent.emit(false);
        this.newTag = "";
        this.searchedTag = "";
    }

    removeTag(tag: string) {
        let index = this.selectedTags.indexOf(tag);
        this.selectedTags.splice(index, 1);
    }

    async tagChange() {
        let tags = await firstValueFrom(this.tagService.tags$);
        let isIn = tags.includes(this.searchedTag.toLowerCase());

        if (!isIn) this.newTag = this.searchedTag;
        else this.newTag = "";
    }
}
