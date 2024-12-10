import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';

import { IMessage, ISaveResult, MessageType } from '@usnistgov/ngx-dam-framework';
import { HttpClient } from '@angular/common/http';
import { IAPIKeyCreateRequest, IAPIKeyDisplay } from '../models/api-key';

@Injectable({
  providedIn: 'root',
})
export class ApiKeyService {
  readonly APIKEY_END_POINT = '/api/api-keys';
  constructor(private http: HttpClient) { }

  getApiKeys(): Observable<IAPIKeyDisplay[]> {
    return this.http.get<IAPIKeyDisplay[]>(`${this.APIKEY_END_POINT}`);
  }

  createApiKey(apiKey: IAPIKeyCreateRequest): Observable<IMessage<IAPIKeyDisplay>> {
    return this.http.post<IMessage<IAPIKeyDisplay>>(`${this.APIKEY_END_POINT}`, apiKey);
  }
  deleteApiKey(id: string): Observable<IMessage<string>> {
    return this.http.delete<IMessage<string>>(`${this.APIKEY_END_POINT}/${id}`);
  }

}
