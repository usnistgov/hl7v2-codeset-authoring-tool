import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import {
  DamMainComponent,
  UserManagementHeaderComponent,
} from '@usnistgov/ngx-dam-framework';
import { HeaderComponent } from './core/components/header/header.component';
import {
  FontAwesomeModule,
  FaIconLibrary,
} from '@fortawesome/angular-fontawesome';
import {
  faCalendar,
  faCheck,
  faChevronDown,
  faChevronRight,
  faCopy,
  faDownload,
  faFile,
  faFilter,
  faInfo,
  faInfoCircle,
  faList,
  faMinus,
  faPencil,
  faPlus,
  faSearch,
  faTable,
  faTimes,
  faTrashCan,
  faUser,
} from '@fortawesome/free-solid-svg-icons';
import { HEADERS } from './headers.configuration';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    HeaderComponent,
    CommonModule,
    DamMainComponent,
    UserManagementHeaderComponent,
    RouterOutlet,
    RouterModule,
    FontAwesomeModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'Code sets Authoring Tool';
  abbrv = 'CAT';
  version = 'v1.0.0';
  menuOptions = HEADERS;

  // Hint (Icons): add icons from font-awesome here, to reference in the rest of the app
  constructor(iconLibrary: FaIconLibrary) {
    iconLibrary.addIcons(faUser);
    iconLibrary.addIcons(faFile);
    iconLibrary.addIcons(faPencil);
    iconLibrary.addIcons(faList);
    iconLibrary.addIcons(faFilter);
    iconLibrary.addIcons(faPlus);
    iconLibrary.addIcons(faMinus);
    iconLibrary.addIcons(faChevronRight);
    iconLibrary.addIcons(faChevronDown);
    iconLibrary.addIcons(faCheck);
    iconLibrary.addIcons(faTrashCan);
    iconLibrary.addIcons(faCalendar);
    iconLibrary.addIcons(faCopy);
    iconLibrary.addIcons(faTable);
    iconLibrary.addIcons(faDownload);
    iconLibrary.addIcons(faTimes);
    iconLibrary.addIcons(faSearch);
    iconLibrary.addIcons(faInfoCircle);









  }
}
