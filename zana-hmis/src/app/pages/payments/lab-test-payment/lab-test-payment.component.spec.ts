import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestPaymentComponent } from './lab-test-payment.component';

describe('LabTestPaymentComponent', () => {
  let component: LabTestPaymentComponent;
  let fixture: ComponentFixture<LabTestPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabTestPaymentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabTestPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
