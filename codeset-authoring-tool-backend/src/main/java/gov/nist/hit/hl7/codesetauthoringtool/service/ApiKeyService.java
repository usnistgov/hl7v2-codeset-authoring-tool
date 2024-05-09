package gov.nist.hit.hl7.codesetauthoringtool.service;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;

import java.io.IOException;
import java.util.List;

public interface ApiKeyService {
    public List<ApiKey> getAllApiKeys() throws IOException;
    public ApiKey createApiKey(ApiKey apiKey) throws  IOException;
}
