import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardTagsComponent } from './snippet-card-tags.component';

describe('SnippetCardTagsComponent', () => {
  let component: SnippetCardTagsComponent;
  let fixture: ComponentFixture<SnippetCardTagsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardTagsComponent]
    });
    fixture = TestBed.createComponent(SnippetCardTagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
