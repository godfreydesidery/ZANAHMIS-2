import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalMedicalProviderComponent } from './external-medical-provider.component';

describe('ExternalMedicalProviderComponent', () => {
  let component: ExternalMedicalProviderComponent;
  let fixture: ComponentFixture<ExternalMedicalProviderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExternalMedicalProviderComponent]
    });
    fixture = TestBed.createComponent(ExternalMedicalProviderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
