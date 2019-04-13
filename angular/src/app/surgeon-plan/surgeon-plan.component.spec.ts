import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SurgeonPlanComponent } from './surgeon-plan.component';

describe('SurgeonPlanComponent', () => {
  let component: SurgeonPlanComponent;
  let fixture: ComponentFixture<SurgeonPlanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SurgeonPlanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SurgeonPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
