import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyDispensingReportComponent } from './pharmacy-dispensing-report.component';

describe('PharmacyDispensingReportComponent', () => {
  let component: PharmacyDispensingReportComponent;
  let fixture: ComponentFixture<PharmacyDispensingReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyDispensingReportComponent]
    });
    fixture = TestBed.createComponent(PharmacyDispensingReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
