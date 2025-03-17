package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetAccessDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetMetadataAccessDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetVersionMetadataAccessDTO;
import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.exception.ResourceAPIAccessDeniedException;
import gov.nist.hit.hl7.codesetauthoringtool.service.CodesetAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/access/codesets")
public class CodesetAccessController {
    private final CodesetAccessService codesetAccessService;

    public CodesetAccessController(CodesetAccessService codesetAccessService) {
        this.codesetAccessService = codesetAccessService;
    }

    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CodesetAccessDTO> getCodeset(@RequestHeader(name = "X-API-KEY", required = false) String apiKey, @PathVariable String id,
                                                       @RequestParam(name = "version", required = false) String version,
                                                       @RequestParam(name = "match", required = false) String match) throws IOException, ResourceAPIAccessDeniedException, NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetAccessDTO codeset = codesetAccessService.getCodeset(id, version, match, apiKey);
        return new ResponseEntity<>(codeset, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/metadata", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CodesetMetadataAccessDTO> getCodesetMetadata(@RequestHeader(name = "X-API-KEY", required = false) String apiKey, @PathVariable String id
    ) throws IOException, ResourceAPIAccessDeniedException, NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetMetadataAccessDTO codeset = codesetAccessService.getCodesetMetadata(id, apiKey);
        return new ResponseEntity<>(codeset, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/versions/{version}/metadata", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CodesetVersionMetadataAccessDTO> getCodesetVersionMetadata(@RequestHeader(name = "X-API-KEY", required = false) String apiKey,
                                                                              @PathVariable String id, @PathVariable String version
    ) throws IOException, ResourceAPIAccessDeniedException, NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetVersionMetadataAccessDTO codeset = codesetAccessService.getCodesetVersionMetadata(id,version, apiKey);
        return new ResponseEntity<>(codeset, HttpStatus.OK);
    }
}
