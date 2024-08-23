import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacySalesReportComponent } from './pharmacy-sales-report.component';

describe('PharmacySalesReportComponent', () => {
  let component: PharmacySalesReportComponent;
  let fixture: ComponentFixture<PharmacySalesReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacySalesReportComponent]
    });
    fixture = TestBed.createComponent(PharmacySalesReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
