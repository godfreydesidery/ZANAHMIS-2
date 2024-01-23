import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabSampleCollectionReportComponent } from './lab-sample-collection-report.component';

describe('LabSampleCollectionReportComponent', () => {
  let component: LabSampleCollectionReportComponent;
  let fixture: ComponentFixture<LabSampleCollectionReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LabSampleCollectionReportComponent]
    });
    fixture = TestBed.createComponent(LabSampleCollectionReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
