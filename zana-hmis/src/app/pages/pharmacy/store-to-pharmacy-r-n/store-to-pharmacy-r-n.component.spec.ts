import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StoreToPharmacyRNComponent } from './store-to-pharmacy-r-n.component';

describe('StoreToPharmacyRNComponent', () => {
  let component: StoreToPharmacyRNComponent;
  let fixture: ComponentFixture<StoreToPharmacyRNComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StoreToPharmacyRNComponent]
    });
    fixture = TestBed.createComponent(StoreToPharmacyRNComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
