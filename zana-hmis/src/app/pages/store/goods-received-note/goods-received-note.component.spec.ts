import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GoodsReceivedNoteComponent } from './goods-received-note.component';

describe('GoodsReceivedNoteComponent', () => {
  let component: GoodsReceivedNoteComponent;
  let fixture: ComponentFixture<GoodsReceivedNoteComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GoodsReceivedNoteComponent]
    });
    fixture = TestBed.createComponent(GoodsReceivedNoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
