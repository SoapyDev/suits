import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LegalFileDialogComponent } from './legal-file-dialog.component';

describe('LegalFileDialogComponent', () => {
  let component: LegalFileDialogComponent;
  let fixture: ComponentFixture<LegalFileDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LegalFileDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LegalFileDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
