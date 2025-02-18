import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { damfRouteConfig, LoginComponent } from '@usnistgov/ngx-dam-framework';
import { ForgotPasswordComponent } from './core/components/forgot-password/forgot-password.component';
import { ErrorPageComponent } from './core/components/error-page/error-page.component';

import {
  CodesetListComponent,
  CodesetListState,
} from './codeset/components/codeset-list/codeset-list.component';
import { CODESET_WIDGET_ID, CodesetState, CodesetVersionsState, CodesetWidgetComponent } from './codeset/components/codeset-widget/codeset-widget.component';
import { CodesetCreateComponent } from './codeset/components/codeset-create/codeset-create.component';
import { CODESET_VERSION_EDITOR_INITIALIZER, CodesetVersionEditorComponent } from './codeset/components/codeset-version-editor/codeset-version-editor.component';
import { CODESET_METADATA_EDITOR_INITIALIZER, CodesetMetadataEditorComponent } from './codeset/components/codeset-metadata-editor/codeset-metadata-editor.component';
import { UsersListComponent, UsersListState } from './users/components/users-list/users-list.component';
import { UserCreateComponent } from './users/components/user-create/user-create.component';
import { ApiKeyListComponent, ApiKeyListState } from './api-key/components/api-key-list/api-key-list.component';
import { ApiKeyCreateComponent } from './api-key/components/api-key-create/api-key-create.component';
import { UserEditComponent } from './users/components/user-edit/user-edit.component';

const DEFAULT_ERROR_URL = () => ({ command: ['/', 'error'] });

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'login',
    ...damfRouteConfig()
      .useNotAuthenticated()
      .useMessaging({
        tags: ['LOGIN_ISSUES'],
      }),
    component: LoginComponent,
  },
  {
    path: 'forgot-password',
    ...damfRouteConfig()
      .useNotAuthenticated()
      .useMessaging({
        tags: ['FP_ISSUES'],
      })
      .build(),
    component: ForgotPasswordComponent,
  },

  {
    path: 'codesets',
    children: [
      {
        path: 'list',
        ...damfRouteConfig()
          .useAuthenticated()
          .useLoadData(CodesetListState)
          .useErrorURL(DEFAULT_ERROR_URL)
          .useMessaging({})
          .build(),
        component: CodesetListComponent,
      },
      {
        path: 'create',
        ...damfRouteConfig()
          .useAuthenticated()
          .useMessaging({
            tags: ['CREATE_ENTITY_ISSUES'],
          })
          .build(),
        component: CodesetCreateComponent,
      },
      {
        path: ':codesetId',
        ...damfRouteConfig()
          .useWidget({
            widgetId: CODESET_WIDGET_ID,
            component: CodesetWidgetComponent,
          })
          .useAuthenticated()
          .useLoadManyData([CodesetState, CodesetVersionsState])
          .useErrorURL(DEFAULT_ERROR_URL)
          .useMessaging({})
          .withChildren([
            {
              path: 'versions/:versionId',
              ...damfRouteConfig()
                .useAuthenticated()
                .useEditor({
                  component: CodesetVersionEditorComponent,
                  initializer: CODESET_VERSION_EDITOR_INITIALIZER,
                  saveOnRouteExit: true,
                })
                .useErrorURL(DEFAULT_ERROR_URL)
                .useMessaging({})
                .build(),
            },
            {
              path: 'dashboard',
              ...damfRouteConfig()
                .useAuthenticated()
                .useEditor({
                  component: CodesetMetadataEditorComponent,
                  initializer: CODESET_METADATA_EDITOR_INITIALIZER,
                  saveOnRouteExit: true,
                })
                .useErrorURL(DEFAULT_ERROR_URL)
                .useMessaging({})
                .build(),
            },

          ]),
      },
    ],
  },
  {
    path: 'users',
    children: [
      {
        path: 'list',
        ...damfRouteConfig()
          .useAuthenticated()
          .useLoadData(UsersListState)
          .useErrorURL(DEFAULT_ERROR_URL)
          .useMessaging({})
          .build(),
        component: UsersListComponent,
      },
      {
        path: 'create',
        ...damfRouteConfig()
          .useAuthenticated()
          .useMessaging({
            tags: ['CREATE_ENTITY_ISSUES'],
          })
          .build(),
        component: UserCreateComponent,
      },
      {
        path: 'edit/:id',
        ...damfRouteConfig()
          .useAuthenticated()
          .useMessaging({
            tags: ['CREATE_ENTITY_ISSUES'],
          })
          .build(),
        component: UserEditComponent,
      },

    ],
  },
  {
    path: 'api-keys',
    children: [
      {
        path: 'list',
        ...damfRouteConfig()
          .useAuthenticated()
          .useLoadData(ApiKeyListState)
          .useErrorURL(DEFAULT_ERROR_URL)
          .useMessaging({})
          .build(),
        component: ApiKeyListComponent,
      },
      {
        path: 'create',
        ...damfRouteConfig()
          .useAuthenticated()
          .useMessaging({
            tags: ['CREATE_ENTITY_ISSUES'],
          })
          .build(),
        component: ApiKeyCreateComponent,
      },

    ],
  },
  {
    path: 'error',
    component: ErrorPageComponent,
  },
];
