import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyToPharmacyROListComponent } from './pharmacy-to-pharmacy-r-o-list.component';

describe('PharmacyToPharmacyROListComponent', () => {
  let component: PharmacyToPharmacyROListComponent;
  let fixture: ComponentFixture<PharmacyToPharmacyROListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyToPharmacyROListComponent]
    });
    fixture = TestBed.createComponent(PharmacyToPharmacyROListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
