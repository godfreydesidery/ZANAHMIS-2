import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseOutsiderChartComponent } from './nurse-outsider-chart.component';

describe('NurseOutsiderChartComponent', () => {
  let component: NurseOutsiderChartComponent;
  let fixture: ComponentFixture<NurseOutsiderChartComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseOutsiderChartComponent]
    });
    fixture = TestBed.createComponent(NurseOutsiderChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
