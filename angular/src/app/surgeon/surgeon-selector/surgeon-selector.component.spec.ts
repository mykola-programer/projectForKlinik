import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SurgeonSelectorComponent } from './surgeon-selector.component';

describe('SurgeonSelectorComponent', () => {
  let component: SurgeonSelectorComponent;
  let fixture: ComponentFixture<SurgeonSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SurgeonSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SurgeonSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
