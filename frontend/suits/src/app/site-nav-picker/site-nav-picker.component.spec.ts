import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteNavPickerComponent } from './site-nav-picker.component';

describe('SiteNavPickerComponent', () => {
  let component: SiteNavPickerComponent;
  let fixture: ComponentFixture<SiteNavPickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiteNavPickerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SiteNavPickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
