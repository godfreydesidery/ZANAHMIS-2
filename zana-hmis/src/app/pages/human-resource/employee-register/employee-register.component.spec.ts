import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeRegisterComponent } from './employee-register.component';

describe('EmployeeRegisterComponent', () => {
  let component: EmployeeRegisterComponent;
  let fixture: ComponentFixture<EmployeeRegisterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeRegisterComponent]
    });
    fixture = TestBed.createComponent(EmployeeRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
