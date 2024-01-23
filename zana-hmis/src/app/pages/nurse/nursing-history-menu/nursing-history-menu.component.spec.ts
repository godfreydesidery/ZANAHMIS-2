import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NursingHistoryMenuComponent } from './nursing-history-menu.component';

describe('NursingHistoryMenuComponent', () => {
  let component: NursingHistoryMenuComponent;
  let fixture: ComponentFixture<NursingHistoryMenuComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NursingHistoryMenuComponent]
    });
    fixture = TestBed.createComponent(NursingHistoryMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
