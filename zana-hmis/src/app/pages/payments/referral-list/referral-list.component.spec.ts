import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReferralListComponent } from './referral-list.component';

describe('ReferralListComponent', () => {
  let component: ReferralListComponent;
  let fixture: ComponentFixture<ReferralListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReferralListComponent]
    });
    fixture = TestBed.createComponent(ReferralListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
