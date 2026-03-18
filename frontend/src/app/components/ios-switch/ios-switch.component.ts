import { Component, EventEmitter, Input, Output } from "@angular/core";

@Component({
    selector: "app-ios-switch",
    templateUrl: "./ios-switch.component.html",
    styleUrls: ["./ios-switch.component.scss"]
})
export class IosSwitchComponent {
    @Output() changeEvent: EventEmitter<boolean> = new EventEmitter<boolean>();
    @Input() value: boolean = false;
    @Input() label?: string;

    emitChange(event: any) {
        this.changeEvent.emit(event.target.checked);
    }
}
