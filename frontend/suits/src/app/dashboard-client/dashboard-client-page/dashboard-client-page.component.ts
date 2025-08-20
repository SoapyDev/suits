import {Component, inject, signal} from '@angular/core';
import {SiteNavComponent} from '../../site-nav/site-nav.component';
import {Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-client-page',
  imports: [
    SiteNavComponent,
  ],
  templateUrl: './dashboard-client-page.component.html',
  standalone: true,
  styleUrl: './dashboard-client-page.component.scss'
})

export class DashboardClientPageComponent {

  constructor(
    private router: Router
  ) {

  }


}



