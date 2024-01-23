import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyComponent } from './radiology.component';

describe('RadiologyComponent', () => {
  let component: RadiologyComponent;
  let fixture: ComponentFixture<RadiologyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RadiologyComponent]
    });
    fixture = TestBed.createComponent(RadiologyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
