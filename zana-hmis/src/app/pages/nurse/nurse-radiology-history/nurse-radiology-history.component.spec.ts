import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseRadiologyHistoryComponent } from './nurse-radiology-history.component';

describe('NurseRadiologyHistoryComponent', () => {
  let component: NurseRadiologyHistoryComponent;
  let fixture: ComponentFixture<NurseRadiologyHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NurseRadiologyHistoryComponent]
    });
    fixture = TestBed.createComponent(NurseRadiologyHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
