import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyToPharmacyTOComponent } from './pharmacy-to-pharmacy-t-o.component';

describe('PharmacyToPharmacyTOComponent', () => {
  let component: PharmacyToPharmacyTOComponent;
  let fixture: ComponentFixture<PharmacyToPharmacyTOComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyToPharmacyTOComponent]
    });
    fixture = TestBed.createComponent(PharmacyToPharmacyTOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
