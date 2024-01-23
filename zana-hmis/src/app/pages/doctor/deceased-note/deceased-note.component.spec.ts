import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeceasedNoteComponent } from './deceased-note.component';

describe('DeceasedNoteComponent', () => {
  let component: DeceasedNoteComponent;
  let fixture: ComponentFixture<DeceasedNoteComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeceasedNoteComponent]
    });
    fixture = TestBed.createComponent(DeceasedNoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
