import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientInsuranceInvoicesComponent } from './patient-insurance-invoices.component';

describe('PatientInsuranceInvoicesComponent', () => {
  let component: PatientInsuranceInvoicesComponent;
  let fixture: ComponentFixture<PatientInsuranceInvoicesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientInsuranceInvoicesComponent]
    });
    fixture = TestBed.createComponent(PatientInsuranceInvoicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
