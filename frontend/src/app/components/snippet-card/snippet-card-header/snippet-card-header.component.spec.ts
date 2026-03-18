import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardHeaderComponent } from './snippet-card-header.component';

describe('SnippetCardHeaderComponent', () => {
  let component: SnippetCardHeaderComponent;
  let fixture: ComponentFixture<SnippetCardHeaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardHeaderComponent]
    });
    fixture = TestBed.createComponent(SnippetCardHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
