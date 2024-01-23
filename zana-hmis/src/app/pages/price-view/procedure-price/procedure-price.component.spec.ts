import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedurePriceComponent } from './procedure-price.component';

describe('ProcedurePriceComponent', () => {
  let component: ProcedurePriceComponent;
  let fixture: ComponentFixture<ProcedurePriceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcedurePriceComponent]
    });
    fixture = TestBed.createComponent(ProcedurePriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
