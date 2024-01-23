import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestTypeRangeComponent } from './lab-test-type-range.component';

describe('LabTestTypeRangeComponent', () => {
  let component: LabTestTypeRangeComponent;
  let fixture: ComponentFixture<LabTestTypeRangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabTestTypeRangeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabTestTypeRangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
