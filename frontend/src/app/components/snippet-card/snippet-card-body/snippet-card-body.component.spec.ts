import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardBodyComponent } from './snippet-card-body.component';

describe('SnippetCardBodyComponent', () => {
  let component: SnippetCardBodyComponent;
  let fixture: ComponentFixture<SnippetCardBodyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardBodyComponent]
    });
    fixture = TestBed.createComponent(SnippetCardBodyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
