import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseClinicalNoteHistoryComponent } from './nurse-clinical-note-history.component';

describe('NurseClinicalNoteHistoryComponent', () => {
  let component: NurseClinicalNoteHistoryComponent;
  let fixture: ComponentFixture<NurseClinicalNoteHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseClinicalNoteHistoryComponent]
    });
    fixture = TestBed.createComponent(NurseClinicalNoteHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
