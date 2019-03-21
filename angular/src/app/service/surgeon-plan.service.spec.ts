import { TestBed, inject } from '@angular/core/testing';

import { SurgeonPlanService } from './surgeon-plan.service';

describe('SurgeonPlanService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SurgeonPlanService]
    });
  });

  it('should be created', inject([SurgeonPlanService], (service: SurgeonPlanService) => {
    expect(service).toBeTruthy();
  }));
});
