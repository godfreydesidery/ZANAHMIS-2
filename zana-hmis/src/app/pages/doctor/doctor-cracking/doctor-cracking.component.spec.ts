import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorCrackingComponent } from './doctor-cracking.component';

describe('DoctorCrackingComponent', () => {
  let component: DoctorCrackingComponent;
  let fixture: ComponentFixture<DoctorCrackingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoctorCrackingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DoctorCrackingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
