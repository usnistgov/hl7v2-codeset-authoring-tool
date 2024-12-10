import {
  ApplicationConfig,
  importProvidersFrom,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import {
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  provideAlertService,
  provideAuthService,
  provideLoaderState,
  provideMainState,
  provideRouterState,
} from '@usnistgov/ngx-dam-framework';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { BlockUIModule } from 'ng-block-ui';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { AUTHENTICATION_CONFIGURATION } from './authentication.configuration';
import { AuthenticationMockService } from './authentication.service.mock';
import { CodesetEffects } from './codeset/store/codeset.effects';
import { ApiKeyEffects } from './api-key/store/api-key.effects';
import { UserEffects } from './users/store/user.effects';

export const appConfig: ApplicationConfig = {
  providers: [
    // Angular Providers
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    importProvidersFrom(BrowserAnimationsModule),
    // NgRx Providers
    importProvidersFrom(StoreModule.forRoot()),
    importProvidersFrom(EffectsModule.forRoot([CodesetEffects, ApiKeyEffects, UserEffects])),
    importProvidersFrom(
      StoreDevtoolsModule.instrument({
        maxAge: 25,
      })
    ),
    // Block UI Providers
    importProvidersFrom(BlockUIModule.forRoot()),
    // DAM Loader
    provideLoaderState(),
    // DAM Alerts
    ...provideAlertService(),
    // DAM Authentication
    provideAuthService({
      // Hint (Authentication) : Remove this line to not mock the authentication service
      // useClass: AuthenticationMockService,
      urls: AUTHENTICATION_CONFIGURATION,
    }),
    // DAM Router
    ...provideRouterState(),
    // DAM Main State
    provideMainState(),
  ],
};
