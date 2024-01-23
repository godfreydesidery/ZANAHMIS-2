import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicinePriceComponent } from './medicine-price.component';

describe('MedicinePriceComponent', () => {
  let component: MedicinePriceComponent;
  let fixture: ComponentFixture<MedicinePriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MedicinePriceComponent]
    });
    fixture = TestBed.createComponent(MedicinePriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
