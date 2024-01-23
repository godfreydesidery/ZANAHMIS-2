import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GoodsReceivedNoteReportComponent } from './goods-received-note-report.component';

describe('GoodsReceivedNoteReportComponent', () => {
  let component: GoodsReceivedNoteReportComponent;
  let fixture: ComponentFixture<GoodsReceivedNoteReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GoodsReceivedNoteReportComponent]
    });
    fixture = TestBed.createComponent(GoodsReceivedNoteReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
