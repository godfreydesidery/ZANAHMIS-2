import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestTypeComponent } from './lab-test-type.component';

describe('LabTestTypeComponent', () => {
  let component: LabTestTypeComponent;
  let fixture: ComponentFixture<LabTestTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabTestTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabTestTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
