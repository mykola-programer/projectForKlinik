import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SurgeonEditorComponent } from './surgeon-editor.component';

describe('SurgeonEditorComponent', () => {
  let component: SurgeonEditorComponent;
  let fixture: ComponentFixture<SurgeonEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SurgeonEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SurgeonEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
