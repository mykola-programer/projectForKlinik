import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RelocationDialogComponent } from './relocation-dialog.component';

describe('RelocationDialogComponent', () => {
  let component: RelocationDialogComponent;
  let fixture: ComponentFixture<RelocationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RelocationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RelocationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
