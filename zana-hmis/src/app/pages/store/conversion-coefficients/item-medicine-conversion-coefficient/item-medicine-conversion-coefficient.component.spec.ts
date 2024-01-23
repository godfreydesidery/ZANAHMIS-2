import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemMedicineConversionCoefficientComponent } from './item-medicine-conversion-coefficient.component';

describe('ItemMedicineConversionCoefficientComponent', () => {
  let component: ItemMedicineConversionCoefficientComponent;
  let fixture: ComponentFixture<ItemMedicineConversionCoefficientComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemMedicineConversionCoefficientComponent]
    });
    fixture = TestBed.createComponent(ItemMedicineConversionCoefficientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
