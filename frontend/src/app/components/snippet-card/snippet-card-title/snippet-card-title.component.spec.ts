import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardTitleComponent } from './snippet-card-title.component';

describe('SnippetCardTitleComponent', () => {
  let component: SnippetCardTitleComponent;
  let fixture: ComponentFixture<SnippetCardTitleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardTitleComponent]
    });
    fixture = TestBed.createComponent(SnippetCardTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
