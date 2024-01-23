import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WardPlanComponent } from './ward-plan.component';

describe('WardPlanComponent', () => {
  let component: WardPlanComponent;
  let fixture: ComponentFixture<WardPlanComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WardPlanComponent]
    });
    fixture = TestBed.createComponent(WardPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
