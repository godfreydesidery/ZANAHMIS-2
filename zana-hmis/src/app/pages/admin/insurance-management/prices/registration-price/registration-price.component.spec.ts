import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationPriceComponent } from './registration-price.component';

describe('RegistrationPriceComponent', () => {
  let component: RegistrationPriceComponent;
  let fixture: ComponentFixture<RegistrationPriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationPriceComponent]
    });
    fixture = TestBed.createComponent(RegistrationPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
