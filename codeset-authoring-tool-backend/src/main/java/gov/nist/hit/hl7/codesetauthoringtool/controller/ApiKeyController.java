package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.dto.ApiKeyDTO;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.GeneratedApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.ApiKeyRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
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
@RequestMapping("/api/api-keys")
public class ApiKeyController {
    public final ApiKeyServiceImpl apiKeyService;

    public ApiKeyController(ApiKeyServiceImpl apiKeyService){
    this.apiKeyService = apiKeyService;
    }

    @RequestMapping(value = "", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<ApiKeyDTO>> getApiKeys() throws IOException {
        List<ApiKeyDTO> apiKeys =  this.apiKeyService.getAllApiKeys();
        return ResponseEntity.ok(apiKeys);
    }

    @RequestMapping(value = "", produces = "application/json", method = RequestMethod.POST)
    public ResponseMessage<GeneratedApiKey> createApiKey(@Valid @RequestBody ApiKeyRequest request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GeneratedApiKey newApiKey = this.apiKeyService.createApiKey(request, authentication.getName());
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Api Key created Successfully", newApiKey.getId(), null, newApiKey);

    }

    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseMessage<Void> deleteApiKey(@PathVariable String id) throws IOException {
        this.apiKeyService.deleteApiKey(id);
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Api Key successfully deleted");
    }
}
