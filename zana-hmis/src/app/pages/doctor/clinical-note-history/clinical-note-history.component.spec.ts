import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClinicalNoteHistoryComponent } from './clinical-note-history.component';

describe('ClinicalNoteHistoryComponent', () => {
  let component: ClinicalNoteHistoryComponent;
  let fixture: ComponentFixture<ClinicalNoteHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClinicalNoteHistoryComponent]
    });
    fixture = TestBed.createComponent(ClinicalNoteHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
