import { Component } from '@angular/core';
import {MatTab, MatTabGroup} from '@angular/material/tabs';
import {CaseListingComponent} from '../case/case-listing/case-listing.component';
import {MatIconAnchor, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {TaskListingComponent} from '../task/components/task-listing/task-listing.component';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-site-nav-picker',
  imports: [
    MatTabGroup,
    MatTab,
    CaseListingComponent,
    MatIcon,
    MatIconAnchor,
    MatIconButton,
    TaskListingComponent
  ],
  templateUrl: './site-nav-picker.component.html',
  styleUrl: './site-nav-picker.component.scss'
})
export class SiteNavPickerComponent {

  constructor(private cookieService: CookieService,
              private router: Router) {}

  logout(): void {
    sessionStorage.clear();
    localStorage.clear();
    this.cookieService.deleteAll();
    this.router.navigate(['/']);
  }


}
