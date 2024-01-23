import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadiologyOutsiderListComponent } from './radiology-outsider-list.component';

describe('RadiologyOutsiderListComponent', () => {
  let component: RadiologyOutsiderListComponent;
  let fixture: ComponentFixture<RadiologyOutsiderListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RadiologyOutsiderListComponent]
    });
    fixture = TestBed.createComponent(RadiologyOutsiderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
