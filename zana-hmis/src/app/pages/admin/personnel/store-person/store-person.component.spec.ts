import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorePersonComponent } from './store-person.component';

describe('StorePersonComponent', () => {
  let component: StorePersonComponent;
  let fixture: ComponentFixture<StorePersonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StorePersonComponent]
    });
    fixture = TestBed.createComponent(StorePersonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
