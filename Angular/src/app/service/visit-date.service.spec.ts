import { TestBed, inject } from '@angular/core/testing';

import { VisitDateService } from './visit-date.service';

describe('VisitDateService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VisitDateService]
    });
  });

  it('should be created', inject([VisitDateService], (service: VisitDateService) => {
    expect(service).toBeTruthy();
  }));
});
