import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';

import { IMessage, ISaveResult, MessageType } from '@usnistgov/ngx-dam-framework';
import { HttpClient } from '@angular/common/http';
import { IAPIKeyCreateRequest, IAPIKeyDisplay } from '../models/api-key';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  readonly AUTH_END_POINT = '/api/auth/v1';
  constructor(private http: HttpClient) { }

  sendResetLink(email: string): Observable<IMessage<string>> {
    return this.http.post<IMessage<string>>(`${this.AUTH_END_POINT}/forgot-password`, { email });
  }
  resetPassword(token: string, password: string): Observable<IMessage<string>> {
    return this.http.post<IMessage<string>>(`${this.AUTH_END_POINT}/reset-password`, { token, password });
  }


}
