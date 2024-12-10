import { ICodesetMetadata } from "../../codeset/models/codeset";

export interface IAPIKeyDisplay {
    id: string;
    name: string;
    createdAt: Date;
    expireAt: Date;
    expired: boolean;
    codesets: ICodesetMetadata[];
}

export interface IGeneratedAPIKey extends IAPIKeyDisplay {
    plainToken: string;
}

export interface IAPIKeyCreateRequest {
    name: string;
    expires: boolean;
    validityDays: number;
    codesets: ICodesetMetadata[];
}