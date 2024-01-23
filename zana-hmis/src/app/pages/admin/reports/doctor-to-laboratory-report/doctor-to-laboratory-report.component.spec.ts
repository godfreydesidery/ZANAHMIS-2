import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorToLaboratoryReportComponent } from './doctor-to-laboratory-report.component';

describe('DoctorToLaboratoryReportComponent', () => {
  let component: DoctorToLaboratoryReportComponent;
  let fixture: ComponentFixture<DoctorToLaboratoryReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorToLaboratoryReportComponent]
    });
    fixture = TestBed.createComponent(DoctorToLaboratoryReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
