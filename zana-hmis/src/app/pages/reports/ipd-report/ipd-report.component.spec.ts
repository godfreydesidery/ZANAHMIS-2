import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IpdReportComponent } from './ipd-report.component';

describe('IpdReportComponent', () => {
  let component: IpdReportComponent;
  let fixture: ComponentFixture<IpdReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IpdReportComponent]
    });
    fixture = TestBed.createComponent(IpdReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
