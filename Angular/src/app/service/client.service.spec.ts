import { TestBed, inject } from '@angular/core/testing';
import {ClientService} from "./client.service";


describe('PatientService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClientService]
    });
  });

  it('should be created', inject([ClientService], (service: ClientService) => {
    expect(service).toBeTruthy();
  }));
});
