import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationTypeEditorComponent } from './operation-type-editor.component';

describe('OperationTypeEditorComponent', () => {
  let component: OperationTypeEditorComponent;
  let fixture: ComponentFixture<OperationTypeEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OperationTypeEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OperationTypeEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
