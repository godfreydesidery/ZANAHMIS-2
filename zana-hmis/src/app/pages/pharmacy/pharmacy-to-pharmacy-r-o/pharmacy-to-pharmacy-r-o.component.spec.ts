import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyToPharmacyROComponent } from './pharmacy-to-pharmacy-r-o.component';

describe('PharmacyToPharmacyROComponent', () => {
  let component: PharmacyToPharmacyROComponent;
  let fixture: ComponentFixture<PharmacyToPharmacyROComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyToPharmacyROComponent]
    });
    fixture = TestBed.createComponent(PharmacyToPharmacyROComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
