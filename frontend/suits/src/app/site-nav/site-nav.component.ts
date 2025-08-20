import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, signal} from '@angular/core';
import {MatSidenavModule} from '@angular/material/sidenav';
import {NgOptimizedImage} from '@angular/common';
import {MediaMatcher} from '@angular/cdk/layout';
import {MatIcon} from '@angular/material/icon';
import {MatAnchor, MatButton} from '@angular/material/button';

@Component({
  selector: 'app-site-nav',
  imports: [
    MatSidenavModule,
    NgOptimizedImage,
    MatButton,
    MatIcon,
    MatAnchor,
  ],
  templateUrl: './site-nav.component.html',
  styleUrl: './site-nav.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SiteNavComponent {

  protected readonly isMobile = signal(true);
  private readonly _mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;
  protected opened = signal(true);

  constructor() {
    const media = inject(MediaMatcher);
    this._mobileQuery = media.matchMedia('(max-width: 720px)');
    this.isMobile.set(this._mobileQuery.matches);
    this._mobileQueryListener = () => this.isMobile.set(this._mobileQuery.matches);
    this._mobileQuery.addEventListener('change', this._mobileQueryListener);
  }

  protected isConnected() {
    return localStorage.getItem('role') !== null;
  }
}
