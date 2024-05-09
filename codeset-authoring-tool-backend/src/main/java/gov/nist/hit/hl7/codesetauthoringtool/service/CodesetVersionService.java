package gov.nist.hit.hl7.codesetauthoringtool.service;


import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetVersionRequest;

import java.io.IOException;
import java.util.List;

public interface CodesetVersionService {
    public CodesetVersion addVersionToCodeset(String codesetId, CodesetVersionRequest codesetVersionRequest);
    public CodesetVersion getVersionDetails(String codesetId, String version);

}
