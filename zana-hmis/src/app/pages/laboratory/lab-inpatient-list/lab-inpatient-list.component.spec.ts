import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabInpatientListComponent } from './lab-inpatient-list.component';

describe('LabInpatientListComponent', () => {
  let component: LabInpatientListComponent;
  let fixture: ComponentFixture<LabInpatientListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabInpatientListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabInpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
