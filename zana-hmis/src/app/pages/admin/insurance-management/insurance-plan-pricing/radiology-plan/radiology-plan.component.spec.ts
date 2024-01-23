import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyPlanComponent } from './radiology-plan.component';

describe('RadiologyPlanComponent', () => {
  let component: RadiologyPlanComponent;
  let fixture: ComponentFixture<RadiologyPlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RadiologyPlanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RadiologyPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
