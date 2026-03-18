import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardCodeComponent } from './snippet-card-code.component';

describe('SnippetCardCodeComponent', () => {
  let component: SnippetCardCodeComponent;
  let fixture: ComponentFixture<SnippetCardCodeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardCodeComponent]
    });
    fixture = TestBed.createComponent(SnippetCardCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
