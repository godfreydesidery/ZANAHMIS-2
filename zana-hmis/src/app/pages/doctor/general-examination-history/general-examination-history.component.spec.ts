import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneralExaminationHistoryComponent } from './general-examination-history.component';

describe('GeneralExaminationHistoryComponent', () => {
  let component: GeneralExaminationHistoryComponent;
  let fixture: ComponentFixture<GeneralExaminationHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GeneralExaminationHistoryComponent]
    });
    fixture = TestBed.createComponent(GeneralExaminationHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
