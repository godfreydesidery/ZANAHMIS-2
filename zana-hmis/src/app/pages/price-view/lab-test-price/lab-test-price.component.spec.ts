import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestPriceComponent } from './lab-test-price.component';

describe('LabTestPriceComponent', () => {
  let component: LabTestPriceComponent;
  let fixture: ComponentFixture<LabTestPriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LabTestPriceComponent]
    });
    fixture = TestBed.createComponent(LabTestPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
