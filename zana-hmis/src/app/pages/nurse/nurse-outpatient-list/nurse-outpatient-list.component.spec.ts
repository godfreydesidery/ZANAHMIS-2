import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseOutpatientListComponent } from './nurse-outpatient-list.component';

describe('NurseOutpatientListComponent', () => {
  let component: NurseOutpatientListComponent;
  let fixture: ComponentFixture<NurseOutpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseOutpatientListComponent]
    });
    fixture = TestBed.createComponent(NurseOutpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
