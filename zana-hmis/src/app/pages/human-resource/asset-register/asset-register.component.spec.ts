import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssetRegisterComponent } from './asset-register.component';

describe('AssetRegisterComponent', () => {
  let component: AssetRegisterComponent;
  let fixture: ComponentFixture<AssetRegisterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetRegisterComponent]
    });
    fixture = TestBed.createComponent(AssetRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
