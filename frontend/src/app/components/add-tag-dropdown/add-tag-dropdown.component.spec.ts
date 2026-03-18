import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTagDropdownComponent } from './add-tag-dropdown.component';

describe('AddTagDropdownComponent', () => {
  let component: AddTagDropdownComponent;
  let fixture: ComponentFixture<AddTagDropdownComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddTagDropdownComponent]
    });
    fixture = TestBed.createComponent(AddTagDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
