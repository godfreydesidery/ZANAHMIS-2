import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WardTypePriceComponent } from './ward-type-price.component';

describe('WardTypePriceComponent', () => {
  let component: WardTypePriceComponent;
  let fixture: ComponentFixture<WardTypePriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WardTypePriceComponent]
    });
    fixture = TestBed.createComponent(WardTypePriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
