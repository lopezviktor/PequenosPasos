import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { educadorOnlyGuard } from './educador-only.guard';

describe('educadorOnlyGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => educadorOnlyGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
