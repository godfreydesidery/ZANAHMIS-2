import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyToStoreROComponent } from './pharmacy-to-store-r-o.component';

describe('PharmacyToStoreROComponent', () => {
  let component: PharmacyToStoreROComponent;
  let fixture: ComponentFixture<PharmacyToStoreROComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyToStoreROComponent]
    });
    fixture = TestBed.createComponent(PharmacyToStoreROComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
