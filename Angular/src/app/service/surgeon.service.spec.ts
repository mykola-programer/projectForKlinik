import { TestBed, inject } from '@angular/core/testing';

import { SurgeonService } from './surgeon.service';

describe('SurgeonService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SurgeonService]
    });
  });

  it('should be created', inject([SurgeonService], (service: SurgeonService) => {
    expect(service).toBeTruthy();
  }));
});
