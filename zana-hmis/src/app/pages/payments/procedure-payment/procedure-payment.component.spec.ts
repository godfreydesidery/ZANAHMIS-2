import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedurePaymentComponent } from './procedure-payment.component';

describe('ProcedurePaymentComponent', () => {
  let component: ProcedurePaymentComponent;
  let fixture: ComponentFixture<ProcedurePaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProcedurePaymentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcedurePaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
