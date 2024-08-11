import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacySalesOrderListComponent } from './pharmacy-sales-order-list.component';

describe('PharmacySalesOrderListComponent', () => {
  let component: PharmacySalesOrderListComponent;
  let fixture: ComponentFixture<PharmacySalesOrderListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacySalesOrderListComponent]
    });
    fixture = TestBed.createComponent(PharmacySalesOrderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
