import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyToStoreROListComponent } from './pharmacy-to-store-r-o-list.component';

describe('PharmacyToStoreROListComponent', () => {
  let component: PharmacyToStoreROListComponent;
  let fixture: ComponentFixture<PharmacyToStoreROListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyToStoreROListComponent]
    });
    fixture = TestBed.createComponent(PharmacyToStoreROListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
