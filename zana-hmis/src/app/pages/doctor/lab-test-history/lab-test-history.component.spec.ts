import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabTestHistoryComponent } from './lab-test-history.component';

describe('LabTestHistoryComponent', () => {
  let component: LabTestHistoryComponent;
  let fixture: ComponentFixture<LabTestHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LabTestHistoryComponent]
    });
    fixture = TestBed.createComponent(LabTestHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
