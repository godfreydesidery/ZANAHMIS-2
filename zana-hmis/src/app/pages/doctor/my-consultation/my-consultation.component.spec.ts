import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyConsultationComponent } from './my-consultation.component';

describe('MyConsultationComponent', () => {
  let component: MyConsultationComponent;
  let fixture: ComponentFixture<MyConsultationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyConsultationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MyConsultationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
