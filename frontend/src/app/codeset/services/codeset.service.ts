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
  ICodeDelta,
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
  deleteCodesetVersion(codesetId: string, codesetVersionId: string): Observable<IMessage<ICodeset>> {
    return this.http.delete<IMessage<ICodeset>>(`${this.CODESET_END_POINT}${codesetId}/versions/${codesetVersionId}`);
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
    const form = document.createElement('form');
    form.action = `${this.CODESET_END_POINT}${codesetId}/versions/${versionId}/exportCSV`
    form.method = 'POST';
    form.style.display = 'none';
    document.body.appendChild(form);
    form.submit();
  }

  getCodeset(id: string): Observable<ICodeset> {
    return this.http.get<ICodeset>(`${this.CODESET_END_POINT}${id}`);
  }

  getCodeSetDelta(codeSetId: string, sourceVersionId: string, targetVersionId: string): Observable<ICodeDelta[]> {
    return this.http.get<ICodeDelta[]>(this.CODESET_END_POINT + codeSetId + '/compare/' + sourceVersionId + '/' + targetVersionId);
  }
}
