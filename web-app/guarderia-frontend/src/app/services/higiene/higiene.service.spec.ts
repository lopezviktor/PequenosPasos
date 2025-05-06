import { TestBed } from '@angular/core/testing';

import { HigieneService } from './higiene.service';

describe('HigieneService', () => {
  let service: HigieneService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HigieneService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
