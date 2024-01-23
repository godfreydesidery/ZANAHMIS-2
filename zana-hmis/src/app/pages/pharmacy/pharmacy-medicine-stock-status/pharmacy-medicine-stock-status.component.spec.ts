import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyMedicineStockStatusComponent } from './pharmacy-medicine-stock-status.component';

describe('PharmacyMedicineStockStatusComponent', () => {
  let component: PharmacyMedicineStockStatusComponent;
  let fixture: ComponentFixture<PharmacyMedicineStockStatusComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyMedicineStockStatusComponent]
    });
    fixture = TestBed.createComponent(PharmacyMedicineStockStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
