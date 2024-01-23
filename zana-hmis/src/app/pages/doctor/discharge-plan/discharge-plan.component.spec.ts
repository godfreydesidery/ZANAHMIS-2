import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DischargePlanComponent } from './discharge-plan.component';

describe('DischargePlanComponent', () => {
  let component: DischargePlanComponent;
  let fixture: ComponentFixture<DischargePlanComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DischargePlanComponent]
    });
    fixture = TestBed.createComponent(DischargePlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
