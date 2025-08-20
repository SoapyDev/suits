import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardProPageComponent } from './dashboard-pro-page.component';

describe('DashboardProfPageComponent', () => {
  let component: DashboardProPageComponent;
  let fixture: ComponentFixture<DashboardProPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardProPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardProPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
