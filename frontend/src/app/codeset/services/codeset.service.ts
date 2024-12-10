import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import {
  ICodesetDescriptor,
  ICodeset,
  ISection,
  ISectionLinkDisplay,
  SectionType,
  ICodesetCreate,
  ICodesetVersion,
  ICodesetMetadata,
  ICodesetVersionCommit,
} from '../models/codeset';
import { IMessage, ISaveResult, MessageType } from '@usnistgov/ngx-dam-framework';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class CodesetService {
  readonly CODESET_END_POINT = '/api/v1/codesets/';
  constructor(private http: HttpClient) { }

  getCodesetMetadata(codesetId: string): Observable<ICodeset> {
    return this.http.get<ICodeset>(`${this.CODESET_END_POINT}${codesetId}`);
  }

  getCodesetList(): Observable<ICodesetDescriptor[]> {
    return this.http.get<ICodesetDescriptor[]>(`${this.CODESET_END_POINT}metadata`);
  }
  createCodeset(codeset: ICodesetCreate): Observable<ICodeset> {
    return this.http.post<ICodeset>(`${this.CODESET_END_POINT}`, codeset);
  }
  updateCodeset(codeset: ICodesetMetadata, id: string): Observable<IMessage<ICodeset>> {
    return this.http.put<IMessage<ICodeset>>(`${this.CODESET_END_POINT}${id}`, codeset);
  }
  getCodesetVersion(codesetId: string, versionId: string): Observable<ICodesetVersion> {
    return this.http.get<ICodesetVersion>(`${this.CODESET_END_POINT}${codesetId}/versions/${versionId}`);
  }
  updateCodesetVersion(codesetVersion: ICodesetVersion, codesetId: string, versionId: string): Observable<IMessage<ICodesetVersion>> {
    return this.http.post<IMessage<ICodesetVersion>>(`${this.CODESET_END_POINT}${codesetId}/versions/${versionId}`, codesetVersion);
  }
  commitCodesetVersion(codesetVersionCommit: ICodesetVersionCommit, codesetId: string, versionId: string): Observable<IMessage<ICodesetVersion>> {
    return this.http.post<IMessage<ICodesetVersion>>(`${this.CODESET_END_POINT}${codesetId}/versions/${versionId}/commit`, codesetVersionCommit);
  }

  exportCSV(codesetId: string, versionId: string) {
    console.log("1111")
    const form = document.createElement('form');
    form.action = `${this.CODESET_END_POINT}${codesetId}/versions/${versionId}/exportCSV`
    console.log(form)
    form.method = 'POST';
    form.style.display = 'none';
    document.body.appendChild(form);
    form.submit();
  }



  getCodeset(id: string): Observable<ICodeset> {
    return this.http.get<ICodeset>(`${this.CODESET_END_POINT}${id}`);
  }

  getSectionLinkDisplayForEntity(
    id: string
  ): Observable<ISectionLinkDisplay[]> {
    return of([
      {
        id: '1',
        type: SectionType.TEXT,
        label: 'Some Text Section',
      },
      {
        id: '2',
        type: SectionType.FORM,
        label: 'Some Form Section',
      },
    ]);
  }

  getSection(id: string): Observable<ISection> {
    if (id === '1') {
      return of({
        id: '1',
        type: SectionType.TEXT,
        label: 'Some Text Section',
        value:
          'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
      });
    } else if (id === '2') {
      return of({
        id: '2',
        type: SectionType.FORM,
        label: 'Some Form Section',
        fields: [
          {
            key: 'name',
            label: 'Name',
            value: 'This is a name',
          },
          {
            key: 'desc',
            label: 'Description',
            value: 'This is some description',
          },
        ],
      });
    } else {
      return throwError(() => ({
        status: MessageType.FAILED,
        text: 'Section ' + id + ' not found.',
      }));
    }
  }
}
