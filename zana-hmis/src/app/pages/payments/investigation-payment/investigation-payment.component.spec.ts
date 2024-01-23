import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvestigationPaymentComponent } from './investigation-payment.component';

describe('InvestigationPaymentComponent', () => {
  let component: InvestigationPaymentComponent;
  let fixture: ComponentFixture<InvestigationPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InvestigationPaymentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InvestigationPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
