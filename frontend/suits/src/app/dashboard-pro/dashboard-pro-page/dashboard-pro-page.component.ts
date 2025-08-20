import {Component, CUSTOM_ELEMENTS_SCHEMA,} from '@angular/core';
import {SiteNavComponent} from '../../site-nav/site-nav.component';
import {SiteNavPickerComponent} from '../../site-nav-picker/site-nav-picker.component';
import {TasksListComponent} from '../../tasks-list/tasks-list.component';

@Component({
  selector: 'app-dashboard-pro-page',
  imports: [
    SiteNavComponent,
    SiteNavPickerComponent,
    TasksListComponent
  ],
  templateUrl: './dashboard-pro-page.component.html',
  standalone: true,
  styleUrl: './dashboard-pro-page.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})

export class DashboardProPageComponent {
}



