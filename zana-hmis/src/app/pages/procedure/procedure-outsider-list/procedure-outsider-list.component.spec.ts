import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedureOutsiderListComponent } from './procedure-outsider-list.component';

describe('ProcedureOutsiderListComponent', () => {
  let component: ProcedureOutsiderListComponent;
  let fixture: ComponentFixture<ProcedureOutsiderListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcedureOutsiderListComponent]
    });
    fixture = TestBed.createComponent(ProcedureOutsiderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
