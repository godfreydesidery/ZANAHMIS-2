import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacyOutsiderListComponent } from './pharmacy-outsider-list.component';

describe('PharmacyOutsiderListComponent', () => {
  let component: PharmacyOutsiderListComponent;
  let fixture: ComponentFixture<PharmacyOutsiderListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PharmacyOutsiderListComponent]
    });
    fixture = TestBed.createComponent(PharmacyOutsiderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
