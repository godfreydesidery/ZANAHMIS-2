import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedureOutpatientListComponent } from './procedure-outpatient-list.component';

describe('ProcedureOutpatientListComponent', () => {
  let component: ProcedureOutpatientListComponent;
  let fixture: ComponentFixture<ProcedureOutpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcedureOutpatientListComponent]
    });
    fixture = TestBed.createComponent(ProcedureOutpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
