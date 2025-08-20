import { TestBed } from '@angular/core/testing';

import { MemberDialogService } from './member-dialog.service';

describe('MemberDialogService', () => {
  let service: MemberDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MemberDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
