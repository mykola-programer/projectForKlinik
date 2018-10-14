import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NoWardComponent } from './no-ward.component';

describe('NoWardComponent', () => {
  let component: NoWardComponent;
  let fixture: ComponentFixture<NoWardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NoWardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoWardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
