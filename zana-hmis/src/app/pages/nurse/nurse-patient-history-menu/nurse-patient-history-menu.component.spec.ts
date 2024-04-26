import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NursePatientHistoryMenuComponent } from './nurse-patient-history-menu.component';

describe('NursePatientHistoryMenuComponent', () => {
  let component: NursePatientHistoryMenuComponent;
  let fixture: ComponentFixture<NursePatientHistoryMenuComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NursePatientHistoryMenuComponent]
    });
    fixture = TestBed.createComponent(NursePatientHistoryMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
