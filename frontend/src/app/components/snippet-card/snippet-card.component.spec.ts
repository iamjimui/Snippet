import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardComponent } from './snippet-card.component';

describe('SnippetCardComponent', () => {
  let component: SnippetCardComponent;
  let fixture: ComponentFixture<SnippetCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardComponent]
    });
    fixture = TestBed.createComponent(SnippetCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
