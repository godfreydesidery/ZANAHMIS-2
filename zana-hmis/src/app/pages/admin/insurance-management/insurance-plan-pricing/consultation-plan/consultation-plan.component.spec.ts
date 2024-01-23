import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultationPlanComponent } from './consultation-plan.component';

describe('ConsultationPlanComponent', () => {
  let component: ConsultationPlanComponent;
  let fixture: ComponentFixture<ConsultationPlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsultationPlanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsultationPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
