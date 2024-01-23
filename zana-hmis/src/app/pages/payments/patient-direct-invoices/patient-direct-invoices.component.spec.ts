import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientDirectInvoicesComponent } from './patient-direct-invoices.component';

describe('PatientDirectInvoicesComponent', () => {
  let component: PatientDirectInvoicesComponent;
  let fixture: ComponentFixture<PatientDirectInvoicesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientDirectInvoicesComponent]
    });
    fixture = TestBed.createComponent(PatientDirectInvoicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
