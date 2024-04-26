import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseProcedureHistoryComponent } from './nurse-procedure-history.component';

describe('NurseProcedureHistoryComponent', () => {
  let component: NurseProcedureHistoryComponent;
  let fixture: ComponentFixture<NurseProcedureHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseProcedureHistoryComponent]
    });
    fixture = TestBed.createComponent(NurseProcedureHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
