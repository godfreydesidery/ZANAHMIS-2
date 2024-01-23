import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemInquiryComponent } from './item-inquiry.component';

describe('ItemInquiryComponent', () => {
  let component: ItemInquiryComponent;
  let fixture: ComponentFixture<ItemInquiryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemInquiryComponent]
    });
    fixture = TestBed.createComponent(ItemInquiryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
