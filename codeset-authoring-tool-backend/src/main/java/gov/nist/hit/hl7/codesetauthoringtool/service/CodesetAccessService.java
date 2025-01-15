package gov.nist.hit.hl7.codesetauthoringtool.service;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetAccessDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public interface CodesetAccessService {
    public CodesetAccessDTO getCodeset(String id, String version, String match, String apiKey) throws IOException, ResponseStatusException;

}
