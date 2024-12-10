import { CommonModule } from '@angular/common';
import { Component, Injector, Input } from '@angular/core';
import { IsActiveMatchOptions, Params, RouterModule } from '@angular/router';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { map, Observable, of } from 'rxjs';
import { Store } from '@ngrx/store';
import { selectIsLoggedIn, selectRouterURL, selectUsername } from '@usnistgov/ngx-dam-framework';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

export interface IHeaderMenuOptions {
  activeMainMenuClass: string;
  activeSubMenuClass: string;
  menu: IMenuItem[];
  accountMenu?: IAccountMenu;
}

export interface IAccountMenu {
  loggedOut: IMenuItem[];
  loggedIn?: ISubMenuItem[];
}

export interface IMenuItem {
  label: string;
  routerLink?: string;
  queryParams?: Params,
  activeClass?: string;
  children?: ISubMenuItem[];
  faIcon?: string;
  handler?: (injector: Injector) => void;
}

export interface ISubMenuItem {
  label: string;
  routerLink?: string;
  queryParams?: Params,
  activeClass?: string;
  faIcon?: string;
  handler?: (injector: Injector) => void;
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatProgressBarModule,
    NgbDropdownModule,
    FaIconComponent,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  @Input({ required: true })
  title!: string;
  @Input()
  abbrv?: string;
  @Input()
  version?: string;
  @Input()
  menuOptions?: IHeaderMenuOptions
  accountIsLoggedIn: Observable<boolean>;
  accountUsername: Observable<string | undefined>;
  pathMatchingOptions: IsActiveMatchOptions = {
    matrixParams: 'ignored',
    queryParams: 'ignored',
    fragment: 'ignored',
    paths: 'exact'
  };

  public constructor(private store: Store, public injector: Injector) {
    this.accountIsLoggedIn = this.store.select(selectIsLoggedIn);
    this.accountUsername = this.store.select(selectUsername);
  }

  isActiveClass(routerLink: string, activeClass: string): Observable<string> {
    if (routerLink) {
      return this.store.select(selectRouterURL).pipe(
        map(
          (url: string) => {
            return url.startsWith(routerLink) ? ' ' + activeClass : '';
          },
        ),
      );
    }
    return of();
  }
}
