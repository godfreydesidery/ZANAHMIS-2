import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabOutpatientListComponent } from './lab-outpatient-list.component';

describe('LabOutpatientListComponent', () => {
  let component: LabOutpatientListComponent;
  let fixture: ComponentFixture<LabOutpatientListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabOutpatientListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabOutpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
