package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.ApiKeyRequest;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.ApiKeyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/apikeys")
public class ApiKeyController {
    public final ApiKeyServiceImpl apiKeyService;

    public ApiKeyController(ApiKeyServiceImpl apiKeyService){
    this.apiKeyService = apiKeyService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ApiKey>> getApiKeys() throws IOException {
        List<ApiKey> apiKeys =  this.apiKeyService.getAllApiKeys();
        return ResponseEntity.ok(apiKeys);
    }

    @PostMapping("/")
    public ResponseEntity<ApiKey> createApiKey(@Valid @RequestBody ApiKeyRequest request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiKey apiKey = new ApiKey(
                request.getValue(),
                request.getLabel(),
                request.getExpirationDate(),
                authentication.getName()
        );
        ApiKey newApiKey = this.apiKeyService.createApiKey(apiKey);
        return new ResponseEntity<>(newApiKey, HttpStatus.CREATED);

    }
}
