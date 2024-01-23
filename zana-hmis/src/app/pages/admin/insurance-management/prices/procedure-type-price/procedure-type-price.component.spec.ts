import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedureTypePriceComponent } from './procedure-type-price.component';

describe('ProcedureTypePriceComponent', () => {
  let component: ProcedureTypePriceComponent;
  let fixture: ComponentFixture<ProcedureTypePriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcedureTypePriceComponent]
    });
    fixture = TestBed.createComponent(ProcedureTypePriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
