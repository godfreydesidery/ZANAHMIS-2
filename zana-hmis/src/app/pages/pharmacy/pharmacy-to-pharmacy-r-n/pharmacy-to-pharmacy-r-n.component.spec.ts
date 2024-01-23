import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyToPharmacyRNComponent } from './pharmacy-to-pharmacy-r-n.component';

describe('PharmacyToPharmacyRNComponent', () => {
  let component: PharmacyToPharmacyRNComponent;
  let fixture: ComponentFixture<PharmacyToPharmacyRNComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyToPharmacyRNComponent]
    });
    fixture = TestBed.createComponent(PharmacyToPharmacyRNComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
