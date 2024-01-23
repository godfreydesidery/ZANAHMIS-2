import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StoreItemStockStatusComponent } from './store-item-stock-status.component';

describe('StoreItemStockStatusComponent', () => {
  let component: StoreItemStockStatusComponent;
  let fixture: ComponentFixture<StoreItemStockStatusComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StoreItemStockStatusComponent]
    });
    fixture = TestBed.createComponent(StoreItemStockStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
