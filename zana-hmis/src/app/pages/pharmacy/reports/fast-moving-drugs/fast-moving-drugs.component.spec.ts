import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FastMovingDrugsComponent } from './fast-moving-drugs.component';

describe('FastMovingDrugsComponent', () => {
  let component: FastMovingDrugsComponent;
  let fixture: ComponentFixture<FastMovingDrugsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FastMovingDrugsComponent]
    });
    fixture = TestBed.createComponent(FastMovingDrugsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
