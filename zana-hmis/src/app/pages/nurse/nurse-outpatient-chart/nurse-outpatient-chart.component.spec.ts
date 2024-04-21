import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseOutpatientChartComponent } from './nurse-outpatient-chart.component';

describe('NurseOutpatientChartComponent', () => {
  let component: NurseOutpatientChartComponent;
  let fixture: ComponentFixture<NurseOutpatientChartComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseOutpatientChartComponent]
    });
    fixture = TestBed.createComponent(NurseOutpatientChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
