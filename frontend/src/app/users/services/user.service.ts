import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';

import { IMessage, ISaveResult, MessageType } from '@usnistgov/ngx-dam-framework';
import { HttpClient } from '@angular/common/http';
import { IUser, IUserData } from '../models/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  readonly USER_END_POINT = '/api/users';
  constructor(private http: HttpClient) { }

  getUsersList(): Observable<IUser[]> {
    return this.http.get<IUser[]>(`${this.USER_END_POINT}`);
  }

  createUser(userDetails: IUserData): Observable<IMessage<IUser>> {
    return this.http.post<IMessage<IUser>>(`${this.USER_END_POINT}`, userDetails);
  }
  deleteUser(id: string): Observable<IMessage<string>> {
    return this.http.delete<IMessage<string>>(`${this.USER_END_POINT}/${id}`);
  }


}
