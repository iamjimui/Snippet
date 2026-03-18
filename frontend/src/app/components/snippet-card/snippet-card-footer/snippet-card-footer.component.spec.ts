import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnippetCardFooterComponent } from './snippet-card-footer.component';

describe('SnippetCardFooterComponent', () => {
  let component: SnippetCardFooterComponent;
  let fixture: ComponentFixture<SnippetCardFooterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnippetCardFooterComponent]
    });
    fixture = TestBed.createComponent(SnippetCardFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
