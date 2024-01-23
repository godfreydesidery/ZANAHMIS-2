import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyPriceComponent } from './radiology-price.component';

describe('RadiologyPriceComponent', () => {
  let component: RadiologyPriceComponent;
  let fixture: ComponentFixture<RadiologyPriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RadiologyPriceComponent]
    });
    fixture = TestBed.createComponent(RadiologyPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
