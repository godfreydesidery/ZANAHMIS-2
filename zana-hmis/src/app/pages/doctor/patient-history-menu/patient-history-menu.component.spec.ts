import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientHistoryMenuComponent } from './patient-history-menu.component';

describe('PatientHistoryMenuComponent', () => {
  let component: PatientHistoryMenuComponent;
  let fixture: ComponentFixture<PatientHistoryMenuComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientHistoryMenuComponent]
    });
    fixture = TestBed.createComponent(PatientHistoryMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
