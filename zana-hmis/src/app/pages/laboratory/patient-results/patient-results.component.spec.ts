import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientResultsComponent } from './patient-results.component';

describe('PatientResultsComponent', () => {
  let component: PatientResultsComponent;
  let fixture: ComponentFixture<PatientResultsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientResultsComponent]
    });
    fixture = TestBed.createComponent(PatientResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
