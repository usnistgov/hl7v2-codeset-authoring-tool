package gov.nist.hit.hl7.codesetauthoringtool.service;

import gov.nist.hit.hl7.codesetauthoringtool.dto.ApiKeyDTO;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.GeneratedApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.ApiKeyRequest;

import java.io.IOException;
import java.util.List;

public interface ApiKeyService {
    public List<ApiKeyDTO> getAllApiKeys() throws IOException;
    public GeneratedApiKey createApiKey(ApiKeyRequest apiKey, String username) throws  IOException;
    public void deleteApiKey(String id) throws IOException;
}
