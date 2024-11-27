package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;
import gov.nist.hit.hl7.codesetauthoringtool.dto.ApiKeyDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetListItemDTO;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.GeneratedApiKey;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.ApiKeyRequest;
import gov.nist.hit.hl7.codesetauthoringtool.repository.ApiKeyRepository;
import gov.nist.hit.hl7.codesetauthoringtool.service.ApiKeyService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }
    @Override
    public List<ApiKeyDTO> getAllApiKeys() throws IOException {

//        return this.apiKeyRepository.findAll().stream().toList();
        return apiKeyRepository.findAll().stream()
                .map(this::convertToApiKeyDTO) // Map each ApiKey to ApiKeyDTO
                .collect(Collectors.toList());
    }
    // Helper method to convert ApiKey to ApiKeyDTO
    private ApiKeyDTO convertToApiKeyDTO(ApiKey apiKey) {
        List<CodesetListItemDTO> codesetDTOs = apiKey.getCodesets().stream()
                .map(this::convertToCodesetDTO) // Map each Codeset to CodesetDTO
                .collect(Collectors.toList());

        ApiKeyDTO dto = new ApiKeyDTO();
        dto.setId(apiKey.getId());
        dto.setName(apiKey.getName());
        dto.setCodesets(codesetDTOs);
        dto.setToken(apiKey.getToken());
        dto.setUsername(apiKey.getUsername());
        dto.setCreatedAt(apiKey.getCreatedAt());
        dto.setExpireAt(apiKey.getExpireAt());
        return dto;
    }
    // Helper method to convert Codeset to CodesetDTO
    private CodesetListItemDTO convertToCodesetDTO(Codeset codeset) {
        return new CodesetListItemDTO(codeset.getId(), codeset.getName());
    }

    @Override
    public GeneratedApiKey createApiKey(ApiKeyRequest request, String username) throws IOException {
        if(request.isExpires() && request.getValidityDays() <= 0) {
            throw new IOException("Number of days before key expires cannot be lower or equals 0");
        }
        if(StringUtils.isBlank(request.getName()) || StringUtils.isBlank(request.getName().trim())) {
            throw new IOException("Name is required");
        }

        if(request.getCodesets() == null || request.getCodesets().size() == 0) {
            throw new IOException("Resources are required");
        }
        ApiKey apiKey = new ApiKey();
        if(request.isExpires()){
            Date now = new Date();
            now = DateUtils.addDays(now, request.getValidityDays());
            apiKey.setExpireAt(now);
        }
        apiKey.setCodesets(request.getCodesets());
        apiKey.setUsername(username);
        String plainToken = generateStrongRandomKey();
        apiKey.setToken(getKeyHash(plainToken));
        apiKey.setCreatedAt(new Date());
        apiKey.setName(request.getName());
        apiKeyRepository.save(apiKey);
        return new GeneratedApiKey(apiKey, plainToken);
    }

    public void deleteApiKey(String id) throws IOException {
        ApiKey apiKey = this.apiKeyRepository.findById(id).orElseThrow(() -> new IOException("Api key not found."));
        this.apiKeyRepository.delete(apiKey);
    }

    private String generateStrongRandomKey() {
        String random = RandomStringUtils.random(32, 0, 0, true, true, null, new SecureRandom());
        return "cat_" + random;
    }

    public String getKeyHash(String key) {
        return DigestUtils.sha256Hex(key);
    }
}
