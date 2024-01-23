import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedureInpatientListComponent } from './procedure-inpatient-list.component';

describe('ProcedureInpatientListComponent', () => {
  let component: ProcedureInpatientListComponent;
  let fixture: ComponentFixture<ProcedureInpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcedureInpatientListComponent]
    });
    fixture = TestBed.createComponent(ProcedureInpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
