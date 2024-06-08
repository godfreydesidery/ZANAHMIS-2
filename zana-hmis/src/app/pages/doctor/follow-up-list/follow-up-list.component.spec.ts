import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowUpListComponent } from './follow-up-list.component';

describe('FollowUpListComponent', () => {
  let component: FollowUpListComponent;
  let fixture: ComponentFixture<FollowUpListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FollowUpListComponent]
    });
    fixture = TestBed.createComponent(FollowUpListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
