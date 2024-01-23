import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyTypeComponent } from './radiology-type.component';

describe('RadiologyTypeComponent', () => {
  let component: RadiologyTypeComponent;
  let fixture: ComponentFixture<RadiologyTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RadiologyTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RadiologyTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
