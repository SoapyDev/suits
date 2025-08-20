import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterPageProfessionalComponent } from './register-page-professional.component';

describe('RegisterPageProfessionalComponent', () => {
  let component: RegisterPageProfessionalComponent;
  let fixture: ComponentFixture<RegisterPageProfessionalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterPageProfessionalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterPageProfessionalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
