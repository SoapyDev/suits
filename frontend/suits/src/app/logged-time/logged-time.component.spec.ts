import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoggedTimeComponent } from './logged-time.component';

describe('LoggedTimeComponent', () => {
  let component: LoggedTimeComponent;
  let fixture: ComponentFixture<LoggedTimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoggedTimeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoggedTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
