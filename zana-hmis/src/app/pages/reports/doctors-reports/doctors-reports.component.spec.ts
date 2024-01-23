import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorsReportsComponent } from './doctors-reports.component';

describe('DoctorsReportsComponent', () => {
  let component: DoctorsReportsComponent;
  let fixture: ComponentFixture<DoctorsReportsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorsReportsComponent]
    });
    fixture = TestBed.createComponent(DoctorsReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
