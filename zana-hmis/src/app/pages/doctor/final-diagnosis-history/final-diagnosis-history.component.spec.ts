import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalDiagnosisHistoryComponent } from './final-diagnosis-history.component';

describe('FinalDiagnosisHistoryComponent', () => {
  let component: FinalDiagnosisHistoryComponent;
  let fixture: ComponentFixture<FinalDiagnosisHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FinalDiagnosisHistoryComponent]
    });
    fixture = TestBed.createComponent(FinalDiagnosisHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
