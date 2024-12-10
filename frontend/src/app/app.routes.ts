import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { damfRouteConfig, LoginComponent } from '@usnistgov/ngx-dam-framework';
import { RegisterComponent } from './core/components/register/register.component';
import { ForgotPasswordComponent } from './core/components/forgot-password/forgot-password.component';
import {
  EntityListComponent,
  EntityListState,
} from './entity/components/entity-list/entity-list.component';
import { ErrorPageComponent } from './core/components/error-page/error-page.component';
import {
  ENTITY_WIDGET_ID,
  EntityState,
  EntityWidgetComponent,
  SectionLinkDisplayState,
} from './entity/components/entity-widget/entity-widget.component';
import {
  EntityTextEditorComponent,
  SECTION_TEXT_EDITOR_INITIALIZER,
} from './entity/components/entity-text-editor/entity-text-editor.component';
import { EntityCreateComponent } from './entity/components/entity-create/entity-create.component';
import {
  EntityFormEditorComponent,
  SECTION_FORM_EDITOR_INITIALIZER,
} from './entity/components/entity-form-editor/entity-form-editor.component';
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

const DEFAULT_ERROR_URL = () => ({ command: ['/', 'error'] });

export const routes: Routes = [
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
    path: 'register',
    ...damfRouteConfig()
      .useNotAuthenticated()
      .useMessaging({
        tags: ['REGISTER_ISSUES'],
      })
      .build(),
    component: RegisterComponent,
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
    path: 'entity',
    children: [
      {
        path: 'list',
        ...damfRouteConfig()
          .useAuthenticated()
          .useLoadData(EntityListState)
          .useErrorURL(DEFAULT_ERROR_URL)
          .useMessaging({})
          .build(),
        component: EntityListComponent,
      },
      {
        path: 'create',
        ...damfRouteConfig()
          .useAuthenticated()
          .useMessaging({
            tags: ['CREATE_ENTITY_ISSUES'],
          })
          .build(),
        component: EntityCreateComponent,
      },
      {
        path: ':entityId',
        ...damfRouteConfig()
          .useWidget({
            widgetId: ENTITY_WIDGET_ID,
            component: EntityWidgetComponent,
          })
          .useAuthenticated()
          .useLoadManyData([EntityState, SectionLinkDisplayState])
          .useErrorURL(DEFAULT_ERROR_URL)
          .useMessaging({})
          .withChildren([
            {
              path: 'text/:textSectionId',
              ...damfRouteConfig()
                .useAuthenticated()
                .useEditor({
                  component: EntityTextEditorComponent,
                  initializer: SECTION_TEXT_EDITOR_INITIALIZER,
                  saveOnRouteExit: true,
                })
                .useErrorURL(DEFAULT_ERROR_URL)
                .useMessaging({})
                .build(),
            },
            {
              path: 'form/:formSectionId',
              ...damfRouteConfig()
                .useAuthenticated()
                .useEditor({
                  component: EntityFormEditorComponent,
                  initializer: SECTION_FORM_EDITOR_INITIALIZER,
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
          .useLoadManyData([CodesetState, CodesetVersionsState, SectionLinkDisplayState])
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
            {
              path: 'text/:textSectionId',
              ...damfRouteConfig()
                .useAuthenticated()
                .useEditor({
                  component: EntityTextEditorComponent,
                  initializer: SECTION_TEXT_EDITOR_INITIALIZER,
                  saveOnRouteExit: true,
                })
                .useErrorURL(DEFAULT_ERROR_URL)
                .useMessaging({})
                .build(),
            },
            {
              path: 'form/:formSectionId',
              ...damfRouteConfig()
                .useAuthenticated()
                .useEditor({
                  component: EntityFormEditorComponent,
                  initializer: SECTION_FORM_EDITOR_INITIALIZER,
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
