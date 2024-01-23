import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorInpatientComponent } from './doctor-inpatient.component';

describe('DoctorInpatientComponent', () => {
  let component: DoctorInpatientComponent;
  let fixture: ComponentFixture<DoctorInpatientComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorInpatientComponent]
    });
    fixture = TestBed.createComponent(DoctorInpatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
