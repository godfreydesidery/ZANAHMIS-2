import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyInpatientListComponent } from './radiology-inpatient-list.component';

describe('RadiologyInpatientListComponent', () => {
  let component: RadiologyInpatientListComponent;
  let fixture: ComponentFixture<RadiologyInpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RadiologyInpatientListComponent]
    });
    fixture = TestBed.createComponent(RadiologyInpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
