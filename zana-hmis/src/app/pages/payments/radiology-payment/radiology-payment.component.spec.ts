import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyPaymentComponent } from './radiology-payment.component';

describe('RadiologyPaymentComponent', () => {
  let component: RadiologyPaymentComponent;
  let fixture: ComponentFixture<RadiologyPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RadiologyPaymentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RadiologyPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
