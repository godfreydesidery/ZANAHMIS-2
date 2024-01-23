import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SlowMovingDrugsComponent } from './slow-moving-drugs.component';

describe('SlowMovingDrugsComponent', () => {
  let component: SlowMovingDrugsComponent;
  let fixture: ComponentFixture<SlowMovingDrugsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SlowMovingDrugsComponent]
    });
    fixture = TestBed.createComponent(SlowMovingDrugsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
