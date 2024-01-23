import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WardTypeComponent } from './ward-type.component';

describe('WardTypeComponent', () => {
  let component: WardTypeComponent;
  let fixture: ComponentFixture<WardTypeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WardTypeComponent]
    });
    fixture = TestBed.createComponent(WardTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
