import { IListEntity } from '@usnistgov/ngx-dam-framework';

export interface ICodesetDescriptor extends IListEntity {
  id: string;
  name: string;
}

export interface ICodesetCreate {
  name: string;
  description: string;
}
export interface ICodesetMetadata extends ICodesetCreate {
  id: string;
}

export interface ICodeset {
  id: string;
  label: string;
  name: string;
  description: string;
  latestVersion: string;
  public: boolean;
  dateCreated: string;
  dateUpdated: string;
  versions: ICodesetVersion[];
  sections: ISectionLink[];
}

export interface ICodesetVersion {
  id: string;
  version: string;
  status?: string;
  exposed?: boolean;
  dateCreated?: string;
  dateCommitted?: string;
  codes: ICodesetVersionCode[];
  codeSystems: string[];
  comments?: string;
  latestStable?: boolean;

}
export interface ICodesetVersionCode {
  id: string;
  code: string;
  system: string | null;
  display: string;
  usage: string;
  description: string;
  comments: string;
  hasPattern: boolean;
  pattern: string;
}

export interface ICodesetVersionCommit {
  version: string;
  comments: string;
  latest: boolean;
  codes: ICodesetVersionCode[];
}

export interface ICodeDelta {
  change: DeltaChange;
  value: ICodePropertyDelta<string>;
  description: ICodePropertyDelta<string>;
  codeSystem: ICodePropertyDelta<string>;
  hasPattern?: ICodePropertyDelta<boolean>;
  usage?: ICodePropertyDelta<string>;
  pattern: ICodePropertyDelta<string>;
  comments: ICodePropertyDelta<string>;
}
export interface ICodePropertyDelta<T> {
  current: T;
  previous: T;
  change: DeltaChange;
}
export enum DeltaChange {
  ADDED = 'ADDED', DELETED = 'DELETED', CHANGED = 'CHANGED', NONE = 'NONE',
}

export enum SectionType {
  TEXT = 'TEXT',
  FORM = 'FORM',
}

export interface ISectionLink {
  id: string;
  type: SectionType;
}

export interface ISectionLinkDisplay extends ISectionLink {
  id: string;
  type: SectionType;
  label: string;
}

export interface ISection extends ISectionLink {
  label: string;
}

export interface ITextSection extends ISection {
  value: string;
}

export interface IFormSection extends ISection {
  fields: IField[];
}

export interface IField {
  key: string;
  label: string;
  value: string;
}
