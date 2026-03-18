import { AfterViewInit, Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements AfterViewInit {

  @Input() total: number = 0
  @Input() itemsPerView: number = 0;
  @Output() current = new EventEmitter<number[]>();

  start = 0;
  end = 0;


  ngAfterViewInit(): void {
    this.start = 1;
    this.end = this.total > this.itemsPerView ? this.itemsPerView : this.total;
  }


  next() {
    if (this.end >= this.total)
      return;

    if (this.total - this.end < this.itemsPerView) {
      this.start = this.end;
      this.end = (this.total - this.end) + this.start;
    } else {
      this.start += this.itemsPerView;
      this.end += this.itemsPerView;
    }
    this.current.emit([this.start, this.end]);
  }

  prev() {
    if (this.start <= 1)
      return;

    if (this.end - this.start < this.itemsPerView - 1) {
      this.start = this.start - this.itemsPerView + 1;
      this.end = this.start + this.itemsPerView - 1;
    } else {
      this.start -= this.itemsPerView;
      this.end -= this.itemsPerView;
    }
    this.current.emit([this.start, this.end]);
  }

}
