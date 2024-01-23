import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultationPricesComponent } from './consultation-prices.component';

describe('ConsultationPriceComponent', () => {
  let component: ConsultationPricesComponent;
  let fixture: ComponentFixture<ConsultationPricesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConsultationPricesComponent]
    });
    fixture = TestBed.createComponent(ConsultationPricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
