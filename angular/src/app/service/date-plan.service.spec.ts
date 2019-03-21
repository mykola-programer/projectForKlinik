import { TestBed, inject } from '@angular/core/testing';
import {DatePlanService} from "./date-plan.service";


describe('DatePlanService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DatePlanService]
    });
  });

  it('should be created', inject([DatePlanService], (service: DatePlanService) => {
    expect(service).toBeTruthy();
  }));
});
