import {
  IAuthenticationConfig,
  MessageType,
} from '@usnistgov/ngx-dam-framework';

// Hint (Authentication) : Update these values to match your backend authentication
export const AUTHENTICATION_CONFIGURATION: IAuthenticationConfig =
  Object.freeze({
    api: {
      login: '/api/auth/v1/login',
      checkAuthStatus: '/api/auth/v1/status',
      logout: '/api/auth/v1/logout',
      checkLinkToken: '',
    },
    forgotPasswordUrl: '/forgot-password',
    loginPageRedirectUrl: '/login',
    unprotectedRedirectUrl: '/home',
    loginSuccessRedirectUrl: '/home',
    sessionTimeoutStatusCodes: [403],
    unauthorized: {
      status: MessageType.FAILED,
      text: 'You do not have permission to access this page',
    },
  });
