package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.CodesetServiceImpl;
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
public class CodesetController {
    private final CodesetServiceImpl codesetService;

    public CodesetController(CodesetServiceImpl codesetService) {
        this.codesetService = codesetService;
    }

    @PostMapping("/")
    public ResponseEntity<Codeset> createCodeset(@Valid @RequestBody CodesetRequest request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Codeset newCodeset = this.codesetService.createCodeset(request);
        return new ResponseEntity<>(newCodeset, HttpStatus.CREATED);

    }

    @GetMapping("/")
    public ResponseEntity<List<Codeset>> getCodesets(@ModelAttribute CodesetSearchCriteria criteria) throws IOException {
        List<Codeset> codesets = codesetService.getCodesets(criteria);
        return new ResponseEntity<>(codesets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Codeset> getCodeset(@PathVariable String id) throws IOException {
        Codeset codeset = codesetService.getCodeset(id);
        return new ResponseEntity<>(codeset, HttpStatus.OK);
    }


}
