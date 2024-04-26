import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NursePrescriptionHistoryComponent } from './nurse-prescription-history.component';

describe('NursePrescriptionHistoryComponent', () => {
  let component: NursePrescriptionHistoryComponent;
  let fixture: ComponentFixture<NursePrescriptionHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NursePrescriptionHistoryComponent]
    });
    fixture = TestBed.createComponent(NursePrescriptionHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
