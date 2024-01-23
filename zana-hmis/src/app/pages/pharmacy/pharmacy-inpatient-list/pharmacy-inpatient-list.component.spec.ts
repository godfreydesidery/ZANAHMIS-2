import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyInpatientListComponent } from './pharmacy-inpatient-list.component';

describe('PharmacyInpatientListComponent', () => {
  let component: PharmacyInpatientListComponent;
  let fixture: ComponentFixture<PharmacyInpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyInpatientListComponent]
    });
    fixture = TestBed.createComponent(PharmacyInpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
