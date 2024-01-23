import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseOutsiderListComponent } from './nurse-outsider-list.component';

describe('NurseOutsiderListComponent', () => {
  let component: NurseOutsiderListComponent;
  let fixture: ComponentFixture<NurseOutsiderListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseOutsiderListComponent]
    });
    fixture = TestBed.createComponent(NurseOutsiderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
