import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestTypePriceComponent } from './lab-test-type-price.component';

describe('LabTestTypePriceComponent', () => {
  let component: LabTestTypePriceComponent;
  let fixture: ComponentFixture<LabTestTypePriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LabTestTypePriceComponent]
    });
    fixture = TestBed.createComponent(LabTestTypePriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
