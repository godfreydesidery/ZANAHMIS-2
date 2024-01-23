import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientReportComponent } from './patient-report.component';

describe('PatientReportComponent', () => {
  let component: PatientReportComponent;
  let fixture: ComponentFixture<PatientReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientReportComponent]
    });
    fixture = TestBed.createComponent(PatientReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
