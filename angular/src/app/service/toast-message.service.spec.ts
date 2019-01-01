import { TestBed, inject } from '@angular/core/testing';

import { ToastMessageService } from './toast-message.service';

describe('ToastMessageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ToastMessageService]
    });
  });

  it('should be created', inject([ToastMessageService], (service: ToastMessageService) => {
    expect(service).toBeTruthy();
  }));
});
