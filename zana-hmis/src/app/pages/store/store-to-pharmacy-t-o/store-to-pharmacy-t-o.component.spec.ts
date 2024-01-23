import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StoreToPharmacyTOComponent } from './store-to-pharmacy-t-o.component';

describe('StoreToPharmacyTOComponent', () => {
  let component: StoreToPharmacyTOComponent;
  let fixture: ComponentFixture<StoreToPharmacyTOComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StoreToPharmacyTOComponent]
    });
    fixture = TestBed.createComponent(StoreToPharmacyTOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
