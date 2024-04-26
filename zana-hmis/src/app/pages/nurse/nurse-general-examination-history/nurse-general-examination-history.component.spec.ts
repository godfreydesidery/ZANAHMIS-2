import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseGeneralExaminationHistoryComponent } from './nurse-general-examination-history.component';

describe('NurseGeneralExaminationHistoryComponent', () => {
  let component: NurseGeneralExaminationHistoryComponent;
  let fixture: ComponentFixture<NurseGeneralExaminationHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseGeneralExaminationHistoryComponent]
    });
    fixture = TestBed.createComponent(NurseGeneralExaminationHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
