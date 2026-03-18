import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IosSwitchComponent } from './ios-switch.component';

describe('IosSwitchComponent', () => {
  let component: IosSwitchComponent;
  let fixture: ComponentFixture<IosSwitchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IosSwitchComponent]
    });
    fixture = TestBed.createComponent(IosSwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
