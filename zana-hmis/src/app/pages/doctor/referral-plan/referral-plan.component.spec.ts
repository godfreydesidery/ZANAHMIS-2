import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReferralPlanComponent } from './referral-plan.component';

describe('ReferralPlanComponent', () => {
  let component: ReferralPlanComponent;
  let fixture: ComponentFixture<ReferralPlanComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReferralPlanComponent]
    });
    fixture = TestBed.createComponent(ReferralPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
