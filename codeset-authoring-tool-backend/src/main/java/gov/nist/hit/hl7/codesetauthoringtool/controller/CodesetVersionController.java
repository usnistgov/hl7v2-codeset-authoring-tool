package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetVersionRequest;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.CodesetServiceImpl;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.CodesetVersionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/codesets")
public class CodesetVersionController {

    private final CodesetVersionServiceImpl codesetVersionService;

    public CodesetVersionController(CodesetVersionServiceImpl codesetVersionService) {
        this.codesetVersionService = codesetVersionService;
    }

    @PostMapping("/{id}/versions")
    public ResponseEntity<CodesetVersion> addVersionToCodeset(@PathVariable String id, @Valid @RequestBody CodesetVersionRequest request) throws IOException {
        CodesetVersion newCodesetVersion = this.codesetVersionService.addVersionToCodeset(id, request);
        return new ResponseEntity<>(newCodesetVersion, HttpStatus.CREATED);
    }
//    @GetMapping("/{id}/versions/{version}")
//    public ResponseEntity<CodesetVersion> getCodesets(@PathVariable String id, @PathVariable String version) throws IOException {
//        CodesetVersion codesetVersion = this.codesetVersionService.getVersionDetails(id, version);
//        return new ResponseEntity<>(codesetVersion, HttpStatus.OK);
//    }
}
