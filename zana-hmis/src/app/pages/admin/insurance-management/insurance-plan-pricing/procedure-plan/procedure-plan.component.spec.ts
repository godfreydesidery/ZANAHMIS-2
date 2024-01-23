import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedurePlanComponent } from './procedure-plan.component';

describe('ProcedurePlanComponent', () => {
  let component: ProcedurePlanComponent;
  let fixture: ComponentFixture<ProcedurePlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProcedurePlanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcedurePlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
