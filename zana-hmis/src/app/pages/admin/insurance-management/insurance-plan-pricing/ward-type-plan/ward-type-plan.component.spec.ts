import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WardTypePlanComponent } from './ward-type-plan.component';

describe('WardTypePlanComponent', () => {
  let component: WardTypePlanComponent;
  let fixture: ComponentFixture<WardTypePlanComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WardTypePlanComponent]
    });
    fixture = TestBed.createComponent(WardTypePlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
