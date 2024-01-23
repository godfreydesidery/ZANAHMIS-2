import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListFromReceptionComponent } from './list-from-reception.component';

describe('ListFromReceptionComponent', () => {
  let component: ListFromReceptionComponent;
  let fixture: ComponentFixture<ListFromReceptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListFromReceptionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListFromReceptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
