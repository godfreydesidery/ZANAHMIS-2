import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CollectionsReportComponent } from './collections-report.component';

describe('CollectionReportComponent', () => {
  let component: CollectionsReportComponent;
  let fixture: ComponentFixture<CollectionsReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CollectionsReportComponent]
    });
    fixture = TestBed.createComponent(CollectionsReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
