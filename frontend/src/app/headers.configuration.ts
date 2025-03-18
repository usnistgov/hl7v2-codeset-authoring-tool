import { Injector } from '@angular/core';
import { IHeaderMenuOptions } from './core/components/header/header.component';
import { Store } from '@ngrx/store';
import { LogoutRequestAction } from '@usnistgov/ngx-dam-framework';
import { Router } from '@angular/router';

// Hint (Header): Update this to configure you header menu options
export const HEADERS: IHeaderMenuOptions = Object.freeze({
  activeMainMenuClass: 'active',
  activeSubMenuClass: 'active-dropdown',
  menu: [
    {
      label: 'Home',
      routerLink: '/home',
      faIcon: 'user',
    },
    {
      label: 'Code sets',
      routerLink: '/codesets',
      children: [
        {
          label: 'Create Code set',
          routerLink: '/codesets/create',
          faIcon: 'add',
        },
        {
          label: 'Code sets List',
          routerLink: '/codesets/list',
          queryParams: { type: 'all' },
        },
      ],
    },
  ],
  accountMenu: {
    loggedOut: [
      {
        label: 'Login',
        routerLink: '/login',
      }
    ],
    loggedIn: [
      {
        label: 'Api Key management',
        handler: (injector: Injector) => {
          const router = injector.get(Router);
          router.navigate(['/api-keys/list'])
        },
      },
      {
        label: 'User management',
        handler: (injector: Injector) => {
          const router = injector.get(Router);
          router.navigate(['/users/list']);
        },
      },
      {
        label: 'Logout',
        handler: (injector: Injector) => {
          const store = injector.get(Store);
          store.dispatch(new LogoutRequestAction());
        },
      },
    ],
  },
});
