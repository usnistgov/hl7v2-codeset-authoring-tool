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
      label: 'Codesets',
      routerLink: '/codesets',
      children: [
        {
          label: 'Create Codeset',
          routerLink: '/codesets/create',
          faIcon: 'add',
        },
        {
          label: 'Codesets List',
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
      },
      {
        label: 'Register',
        routerLink: '/register',
      },
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
          router.navigate(['/users/list'])
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
