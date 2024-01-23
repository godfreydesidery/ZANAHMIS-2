import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationPricesComponent } from './registration-prices.component';

describe('RegistrationPriceComponent', () => {
  let component: RegistrationPricesComponent;
  let fixture: ComponentFixture<RegistrationPricesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationPricesComponent]
    });
    fixture = TestBed.createComponent(RegistrationPricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
