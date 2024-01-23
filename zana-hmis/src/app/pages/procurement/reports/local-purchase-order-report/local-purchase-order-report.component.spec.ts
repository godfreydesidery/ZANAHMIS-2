import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocalPurchaseOrderReportComponent } from './local-purchase-order-report.component';

describe('LocalPurchaseOrderReportComponent', () => {
  let component: LocalPurchaseOrderReportComponent;
  let fixture: ComponentFixture<LocalPurchaseOrderReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LocalPurchaseOrderReportComponent]
    });
    fixture = TestBed.createComponent(LocalPurchaseOrderReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
