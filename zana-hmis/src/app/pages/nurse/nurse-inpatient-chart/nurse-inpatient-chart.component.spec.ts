import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseInpatientChartComponent } from './nurse-inpatient-chart.component';

describe('NurseInpatientChartComponent', () => {
  let component: NurseInpatientChartComponent;
  let fixture: ComponentFixture<NurseInpatientChartComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseInpatientChartComponent]
    });
    fixture = TestBed.createComponent(NurseInpatientChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
