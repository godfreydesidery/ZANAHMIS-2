import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicationPaymentComponent } from './medication-payment.component';

describe('MedicationPaymentComponent', () => {
  let component: MedicationPaymentComponent;
  let fixture: ComponentFixture<MedicationPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MedicationPaymentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicationPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
