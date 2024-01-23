import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseInpatientListComponent } from './nurse-inpatient-list.component';

describe('NurseInpatientListComponent', () => {
  let component: NurseInpatientListComponent;
  let fixture: ComponentFixture<NurseInpatientListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseInpatientListComponent]
    });
    fixture = TestBed.createComponent(NurseInpatientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
