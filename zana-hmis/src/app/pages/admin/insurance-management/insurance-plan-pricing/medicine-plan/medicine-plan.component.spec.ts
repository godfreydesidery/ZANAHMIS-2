import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicinePlanComponent } from './medicine-plan.component';

describe('MedicinePlanComponent', () => {
  let component: MedicinePlanComponent;
  let fixture: ComponentFixture<MedicinePlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MedicinePlanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicinePlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
