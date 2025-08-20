import {Component, signal, WritableSignal} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-not-found-page',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './not-found-page.component.html',
  styleUrl: './not-found-page.component.scss'
})
export class NotFoundPageComponent {
  protected url: WritableSignal<string> = signal('');

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.url.subscribe((event) => {
      this.url.set(event[0].path);
    })
  }
}
