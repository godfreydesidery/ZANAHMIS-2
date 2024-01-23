import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabOutsiderListComponent } from './lab-outsider-list.component';

describe('LabOutsiderListComponent', () => {
  let component: LabOutsiderListComponent;
  let fixture: ComponentFixture<LabOutsiderListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LabOutsiderListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabOutsiderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
