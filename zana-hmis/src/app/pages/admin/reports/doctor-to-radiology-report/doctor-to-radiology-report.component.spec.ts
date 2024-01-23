import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorToRadiologyReportComponent } from './doctor-to-radiology-report.component';

describe('DoctorToRadiologyReportComponent', () => {
  let component: DoctorToRadiologyReportComponent;
  let fixture: ComponentFixture<DoctorToRadiologyReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorToRadiologyReportComponent]
    });
    fixture = TestBed.createComponent(DoctorToRadiologyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
