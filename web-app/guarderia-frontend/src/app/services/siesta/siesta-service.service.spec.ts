import { TestBed } from '@angular/core/testing';

import { SiestaServiceService } from './siesta-service.service';

describe('SiestaServiceService', () => {
  let service: SiestaServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SiestaServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
