import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyOutpatientListComponent } from './radiology-outpatient-list.component';

describe('RadiologyOutpatientListComponent', () => {
  let component: RadiologyOutpatientListComponent;
  let fixture: ComponentFixture<RadiologyOutpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RadiologyOutpatientListComponent]
    });
    fixture = TestBed.createComponent(RadiologyOutpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
