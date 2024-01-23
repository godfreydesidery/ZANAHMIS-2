import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedureReportComponent } from './procedure-report.component';

describe('ProcedureReportComponent', () => {
  let component: ProcedureReportComponent;
  let fixture: ComponentFixture<ProcedureReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcedureReportComponent]
    });
    fixture = TestBed.createComponent(ProcedureReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
