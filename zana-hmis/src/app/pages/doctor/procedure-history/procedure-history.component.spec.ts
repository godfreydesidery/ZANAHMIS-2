import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedureHistoryComponent } from './procedure-history.component';

describe('ProcedureHistoryComponent', () => {
  let component: ProcedureHistoryComponent;
  let fixture: ComponentFixture<ProcedureHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcedureHistoryComponent]
    });
    fixture = TestBed.createComponent(ProcedureHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
