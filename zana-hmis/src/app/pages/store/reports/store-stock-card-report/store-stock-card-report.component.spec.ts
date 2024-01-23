import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StoreStockCardReportComponent } from './store-stock-card-report.component';

describe('StoreStockCardReportComponent', () => {
  let component: StoreStockCardReportComponent;
  let fixture: ComponentFixture<StoreStockCardReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StoreStockCardReportComponent]
    });
    fixture = TestBed.createComponent(StoreStockCardReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
