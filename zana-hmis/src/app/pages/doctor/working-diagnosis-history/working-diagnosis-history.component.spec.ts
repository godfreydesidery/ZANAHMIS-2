import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkingDiagnosisHistoryComponent } from './working-diagnosis-history.component';

describe('WorkingDiagnosisHistoryComponent', () => {
  let component: WorkingDiagnosisHistoryComponent;
  let fixture: ComponentFixture<WorkingDiagnosisHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkingDiagnosisHistoryComponent]
    });
    fixture = TestBed.createComponent(WorkingDiagnosisHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
