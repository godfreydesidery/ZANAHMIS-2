import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyOutpatientListComponent } from './pharmacy-outpatient-list.component';

describe('PharmacyOutpatientListComponent', () => {
  let component: PharmacyOutpatientListComponent;
  let fixture: ComponentFixture<PharmacyOutpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyOutpatientListComponent]
    });
    fixture = TestBed.createComponent(PharmacyOutpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
