import { TestBed } from '@angular/core/testing';

import { EducatorService } from './educator.service';

describe('EducatorService', () => {
  let service: EducatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EducatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
