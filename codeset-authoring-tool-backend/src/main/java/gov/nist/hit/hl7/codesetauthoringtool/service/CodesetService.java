package gov.nist.hit.hl7.codesetauthoringtool.service;


import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetVersionRequest;

import java.io.IOException;
import java.util.List;

public interface CodesetService {

    public Codeset createCodeset(CodesetRequest codesetRequest) throws IOException;
    public List<Codeset> getCodesets(CodesetSearchCriteria codesetSearchCriteria) throws IOException;
    public Codeset getCodeset(String id) throws IOException;


}
