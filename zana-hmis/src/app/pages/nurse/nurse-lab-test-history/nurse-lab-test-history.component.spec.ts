import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseLabTestHistoryComponent } from './nurse-lab-test-history.component';

describe('NurseLabTestHistoryComponent', () => {
  let component: NurseLabTestHistoryComponent;
  let fixture: ComponentFixture<NurseLabTestHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseLabTestHistoryComponent]
    });
    fixture = TestBed.createComponent(NurseLabTestHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
