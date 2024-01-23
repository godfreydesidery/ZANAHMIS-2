import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestStatisticsReportComponent } from './lab-test-statistics-report.component';

describe('LabTestStatisticsReportComponent', () => {
  let component: LabTestStatisticsReportComponent;
  let fixture: ComponentFixture<LabTestStatisticsReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LabTestStatisticsReportComponent]
    });
    fixture = TestBed.createComponent(LabTestStatisticsReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
