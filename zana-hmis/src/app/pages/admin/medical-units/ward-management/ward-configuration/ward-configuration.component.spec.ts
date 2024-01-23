import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WardConfigurationComponent } from './ward-configuration.component';

describe('WardConfigurationComponent', () => {
  let component: WardConfigurationComponent;
  let fixture: ComponentFixture<WardConfigurationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WardConfigurationComponent]
    });
    fixture = TestBed.createComponent(WardConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
