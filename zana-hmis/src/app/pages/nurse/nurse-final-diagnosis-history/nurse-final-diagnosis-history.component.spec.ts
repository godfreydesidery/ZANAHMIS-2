import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseFinalDiagnosisHistoryComponent } from './nurse-final-diagnosis-history.component';

describe('NurseFinalDiagnosisHistoryComponent', () => {
  let component: NurseFinalDiagnosisHistoryComponent;
  let fixture: ComponentFixture<NurseFinalDiagnosisHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseFinalDiagnosisHistoryComponent]
    });
    fixture = TestBed.createComponent(NurseFinalDiagnosisHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
