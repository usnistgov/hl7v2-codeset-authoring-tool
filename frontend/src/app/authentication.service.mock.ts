import { inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { of, Observable, throwError } from 'rxjs';
import { Injectable } from '@angular/core';
import { AuthenticationService, IMessage, MessageType, UtilityService } from '@usnistgov/ngx-dam-framework';

// Hint (Authentication) : delete this mock service when not in use
@Injectable()
export class AuthenticationMockService extends AuthenticationService {

  readonly fni = 'Feature Not Implemented';
  utility = inject(UtilityService);

  constructor(http: HttpClient) {
    super(http);
  }

  override getLoginPageRedirectUrl(): string {
    return '/login';
  }

  override getUnprotectedRedirectUrl(): string {
    return '/home';
  }

  override getLoginSuccessRedirectUrl(): string {
    return '/home';
  }

  override getForgotPasswordUrl(): string {
    return '/forgot-password';
  }

  override login(username: string, password: string): Observable<IMessage<any>> {
    if (password !== 'password') {
      this.utility.clearAlerts(['LOGIN_ISSUES']);
      return this.utility.useEmitErrorAlert(throwError(() => ({
        status: MessageType.FAILED,
        text: 'Invalid password',
      })), { tags: ['LOGIN_ISSUES'] });
    }
    const user = { username, loggedInAt: new Date().getTime() };
    this.setUserInLocalStorage(user);
    return this.utility.useEmitSuccessAlert(of({
      status: MessageType.SUCCESS,
      text: 'Login Success',
      data: { username }
    }));
  }

  requestChangePassword(email: string): Observable<IMessage<string>> {
    throw new Error(this.fni);
  }

  validateToken(token: string): Observable<IMessage<string>> {
    throw new Error(this.fni);
  }

  updatePassword(token: string, password: string): Observable<IMessage<any>> {
    throw new Error(this.fni);
  }

  override checkAuthStatus(): Observable<any> {
    const user = this.getUserFromLocalStorage();
    if (user) {
      const now = new Date().getTime();
      const elapsed = now - user.loggedInAt;
      if (elapsed >= 5 * 60 * 1000) {
        this.removeUserFromLocalStorage();
        return throwError(() => new HttpErrorResponse({
          status: 403,
        }));
      } else {
        return of(user);
      }
    } else {
      return throwError(() => new HttpErrorResponse({
        status: 403,
      }));
    }
  }

  override logout(): Observable<IMessage<any>> {
    this.removeUserFromLocalStorage();
    return of({
      status: MessageType.SUCCESS,
      text: 'Logout Success'
    });
  }

  setUserInLocalStorage(user: any) {
    localStorage.setItem('damt-user-mock', JSON.stringify(user));
  }

  removeUserFromLocalStorage() {
    localStorage.removeItem('damt-user-mock');
  }

  getUserFromLocalStorage(): any {
    const value = localStorage.getItem('damt-user-mock');
    if (value) {
      return JSON.parse(value);
    } else {
      return undefined;
    }
  }

}
