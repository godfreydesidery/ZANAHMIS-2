import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseWorkingDiagnosisHistoryComponent } from './nurse-working-diagnosis-history.component';

describe('NurseWorkingDiagnosisHistoryComponent', () => {
  let component: NurseWorkingDiagnosisHistoryComponent;
  let fixture: ComponentFixture<NurseWorkingDiagnosisHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseWorkingDiagnosisHistoryComponent]
    });
    fixture = TestBed.createComponent(NurseWorkingDiagnosisHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
