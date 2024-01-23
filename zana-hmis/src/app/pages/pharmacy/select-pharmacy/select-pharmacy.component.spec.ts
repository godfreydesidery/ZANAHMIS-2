import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectPharmacyComponent } from './select-pharmacy.component';

describe('SelectPharmacyComponent', () => {
  let component: SelectPharmacyComponent;
  let fixture: ComponentFixture<SelectPharmacyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectPharmacyComponent]
    });
    fixture = TestBed.createComponent(SelectPharmacyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
