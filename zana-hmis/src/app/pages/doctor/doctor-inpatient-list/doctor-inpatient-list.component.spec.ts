import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorInpatientListComponent } from './doctor-inpatient-list.component';

describe('DoctorInpatientListComponent', () => {
  let component: DoctorInpatientListComponent;
  let fixture: ComponentFixture<DoctorInpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorInpatientListComponent]
    });
    fixture = TestBed.createComponent(DoctorInpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
