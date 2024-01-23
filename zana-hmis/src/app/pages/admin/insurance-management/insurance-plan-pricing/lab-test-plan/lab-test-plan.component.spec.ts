import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestPlanComponent } from './lab-test-plan.component';

describe('LabTestPlanComponent', () => {
  let component: LabTestPlanComponent;
  let fixture: ComponentFixture<LabTestPlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabTestPlanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabTestPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
