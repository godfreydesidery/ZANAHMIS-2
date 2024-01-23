import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabPatientListComponent } from './lab-patient-list.component';

describe('LabPatientListComponent', () => {
  let component: LabPatientListComponent;
  let fixture: ComponentFixture<LabPatientListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabPatientListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabPatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
