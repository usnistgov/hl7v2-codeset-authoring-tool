import { Component } from '@angular/core';
import { DamAlertsContainerComponent } from '@usnistgov/ngx-dam-framework';

@Component({
  selector: 'app-error-page',
  standalone: true,
  imports: [
    DamAlertsContainerComponent,
  ],
  templateUrl: './error-page.component.html',
  styleUrl: './error-page.component.scss'
})
export class ErrorPageComponent {

}
