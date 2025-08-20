import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterPageClientComponent } from './register-page-client.component';

describe('RegisterPageClientComponent', () => {
  let component: RegisterPageClientComponent;
  let fixture: ComponentFixture<RegisterPageClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterPageClientComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterPageClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
