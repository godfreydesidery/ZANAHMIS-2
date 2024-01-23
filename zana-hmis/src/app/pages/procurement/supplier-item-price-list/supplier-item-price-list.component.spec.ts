import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierItemPriceListComponent } from './supplier-item-price-list.component';

describe('SupplierItemPriceListComponent', () => {
  let component: SupplierItemPriceListComponent;
  let fixture: ComponentFixture<SupplierItemPriceListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SupplierItemPriceListComponent]
    });
    fixture = TestBed.createComponent(SupplierItemPriceListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
