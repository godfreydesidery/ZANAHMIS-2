import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagnosisTypeComponent } from './diagnosis-type.component';

describe('DiagnosisTypeComponent', () => {
  let component: DiagnosisTypeComponent;
  let fixture: ComponentFixture<DiagnosisTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DiagnosisTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagnosisTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
