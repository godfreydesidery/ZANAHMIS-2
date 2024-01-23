import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyTypePriceComponent } from './radiology-type-price.component';

describe('RadiologyTypePriceComponent', () => {
  let component: RadiologyTypePriceComponent;
  let fixture: ComponentFixture<RadiologyTypePriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RadiologyTypePriceComponent]
    });
    fixture = TestBed.createComponent(RadiologyTypePriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
