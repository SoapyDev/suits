import { TestBed } from '@angular/core/testing';

import { LegalFileService } from './legal-file.service';

describe('LegalFileService', () => {
  let service: LegalFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LegalFileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
