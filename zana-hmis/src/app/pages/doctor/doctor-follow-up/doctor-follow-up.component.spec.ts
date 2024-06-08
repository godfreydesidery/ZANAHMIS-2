import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorFollowUpComponent } from './doctor-follow-up.component';

describe('DoctorFollowUpComponent', () => {
  let component: DoctorFollowUpComponent;
  let fixture: ComponentFixture<DoctorFollowUpComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorFollowUpComponent]
    });
    fixture = TestBed.createComponent(DoctorFollowUpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
