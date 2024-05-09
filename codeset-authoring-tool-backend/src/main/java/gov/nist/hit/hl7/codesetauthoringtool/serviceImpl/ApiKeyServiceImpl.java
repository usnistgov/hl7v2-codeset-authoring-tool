package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.repository.ApiKeyRepository;
import gov.nist.hit.hl7.codesetauthoringtool.service.ApiKeyService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }
    @Override
    public List<ApiKey> getAllApiKeys() throws IOException {
        return this.apiKeyRepository.findAll().stream().toList();
    }

    @Override
    public ApiKey createApiKey(ApiKey apiKey) throws IOException {
        if(apiKey.getValue() == null || apiKey.getValue().trim().isEmpty()) {
            throw new IllegalArgumentException("API Key value cannot be null or empty");
        }
        return apiKeyRepository.save(apiKey);
    }
}
