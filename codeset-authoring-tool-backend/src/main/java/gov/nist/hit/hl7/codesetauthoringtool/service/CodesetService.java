package gov.nist.hit.hl7.codesetauthoringtool.service;


import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetListItemDTO;
import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodeDelta;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetVersionRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CommitRequest;

import java.io.IOException;
import java.util.List;

public interface CodesetService {

    public Codeset createCodeset(CodesetRequest codesetRequest, String username) throws IOException, NotFoundException;
    public CodesetDTO updateCodeset(String id, CodesetRequest codeset, String username) throws IOException;
    public List<CodesetDTO> getCodesets(CodesetSearchCriteria codesetSearchCriteria, String username) throws IOException;
    public List<CodesetListItemDTO> getCodesetsList(CodesetSearchCriteria criteria, String username) throws IOException;
    public CodesetDTO getCodeset(String id, String username) throws IOException;
    public void deleteCodeset(String id, String username) throws IOException, NotFoundException;
    public Codeset cloneCodeset(String id, String username) throws IOException, NotFoundException;
    CodesetVersion getCodesetVersion(String id, String versionId, String username) throws IOException;
    public CodesetVersion saveCodesetVersion(String codesetId, String codesetVersionId, CodesetVersion codesetVersion, String username) throws IOException, NotFoundException;
    public void deleteCodesetVersion(String codesetId, String codesetVersionId,String username) throws IOException, NotFoundException;
    public CodesetVersion commitCodesetVersion(String codesetId, String codesetVersionId, CommitRequest body, String username) throws IOException;
    public List<CodeDelta> getCodeDelta(String codesetId, String codeSetVersionId, String targetId) throws Exception;
}
