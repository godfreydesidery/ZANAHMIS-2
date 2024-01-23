import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InpatientPaymentComponent } from './inpatient-payment.component';

describe('InpatientPaymentComponent', () => {
  let component: InpatientPaymentComponent;
  let fixture: ComponentFixture<InpatientPaymentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InpatientPaymentComponent]
    });
    fixture = TestBed.createComponent(InpatientPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
