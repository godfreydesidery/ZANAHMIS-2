import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyStockCardReportComponent } from './pharmacy-stock-card-report.component';

describe('PharmacyStockCardReportComponent', () => {
  let component: PharmacyStockCardReportComponent;
  let fixture: ComponentFixture<PharmacyStockCardReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyStockCardReportComponent]
    });
    fixture = TestBed.createComponent(PharmacyStockCardReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
