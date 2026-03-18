import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardDescriptionComponent } from './snippet-card-description.component';

describe('SnippetCardDescriptionComponent', () => {
  let component: SnippetCardDescriptionComponent;
  let fixture: ComponentFixture<SnippetCardDescriptionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardDescriptionComponent]
    });
    fixture = TestBed.createComponent(SnippetCardDescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
