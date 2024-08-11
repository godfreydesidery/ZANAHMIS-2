import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacySalesOrderComponent } from './pharmacy-sales-order.component';

describe('PharmacySalesOrderComponent', () => {
  let component: PharmacySalesOrderComponent;
  let fixture: ComponentFixture<PharmacySalesOrderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacySalesOrderComponent]
    });
    fixture = TestBed.createComponent(PharmacySalesOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
