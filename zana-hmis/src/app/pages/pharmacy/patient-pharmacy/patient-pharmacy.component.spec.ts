import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientPharmacyComponent } from './patient-pharmacy.component';

describe('PatientPharmacyComponent', () => {
  let component: PatientPharmacyComponent;
  let fixture: ComponentFixture<PatientPharmacyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientPharmacyComponent]
    });
    fixture = TestBed.createComponent(PatientPharmacyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
