import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TheatreComponent } from './theatre.component';

describe('TheatreComponent', () => {
  let component: TheatreComponent;
  let fixture: ComponentFixture<TheatreComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TheatreComponent]
    });
    fixture = TestBed.createComponent(TheatreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
