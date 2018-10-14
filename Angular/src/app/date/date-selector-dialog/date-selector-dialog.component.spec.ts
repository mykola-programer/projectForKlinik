import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DateSelectorDialogComponent } from './date-selector-dialog.component';

describe('DateSelectorDialogComponent', () => {
  let component: DateSelectorDialogComponent;
  let fixture: ComponentFixture<DateSelectorDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DateSelectorDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DateSelectorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
