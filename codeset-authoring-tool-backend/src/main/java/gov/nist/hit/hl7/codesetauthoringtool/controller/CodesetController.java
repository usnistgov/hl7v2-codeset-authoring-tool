package gov.nist.hit.hl7.codesetauthoringtool.controller;
import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodeDelta;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CommitRequest;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.TableCSVGenerator;
import org.apache.commons.io.IOUtils;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetListItemDTO;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.CodesetServiceImpl;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.CodesetVersionServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/codesets")
public class CodesetController {
    private final CodesetServiceImpl codesetService;
    private final CodesetVersionServiceImpl codesetVersionService;

    public CodesetController(CodesetServiceImpl codesetService, CodesetVersionServiceImpl codesetVersionService) {
        this.codesetService = codesetService;
        this.codesetVersionService = codesetVersionService;
    }

    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<Codeset> createCodeset(@Valid @RequestBody CodesetRequest request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Codeset newCodeset = this.codesetService.createCodeset(request, authentication.getName());
        return new ResponseEntity<>(newCodeset, HttpStatus.CREATED);
    }
    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.PUT)
    @ResponseBody()
    public ResponseMessage<?> updateCodeset(@Valid @RequestBody CodesetRequest request, @PathVariable String id) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetDTO updatedCodeset = this.codesetService.updateCodeset(id, request, authentication.getName());
//        return new ResponseEntity<>(newCodeset, HttpStatus.CREATED);
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Code Set Saved Successfully", updatedCodeset.getId(), null, updatedCodeset);

    }
    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<CodesetDTO>> getCodesets(@ModelAttribute CodesetSearchCriteria criteria) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<CodesetDTO> codesets = codesetService.getCodesets(criteria, authentication.getName());
        return new ResponseEntity<>(codesets, HttpStatus.OK);
    }
    @RequestMapping(value = "/metadata", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<CodesetListItemDTO>> getCodesetsMetadata(@ModelAttribute CodesetSearchCriteria criteria) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<CodesetListItemDTO> codesets = codesetService.getCodesetsList(criteria, authentication.getName());
        return new ResponseEntity<>(codesets, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<CodesetDTO> getCodeset(@PathVariable String id) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetDTO codeset = codesetService.getCodeset(id, authentication.getName());
        return new ResponseEntity<>(codeset, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseMessage<?> deleteCodeset(@PathVariable String id) throws IOException, NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        codesetService.deleteCodeset(id, authentication.getName());
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Code Set Deleted Successfully", null, null, null);
    }


    @RequestMapping(value = "/{id}/versions/{versionId}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<CodesetVersion> getCodesetVersion(@PathVariable String id, @PathVariable String versionId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetVersion codesetVersion = codesetService.getCodesetVersion(id, versionId, authentication.getName());
        return new ResponseEntity<>(codesetVersion, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/versions/{versionId}", produces = "application/json", method = RequestMethod.POST)
    public ResponseMessage<?> saveCodesetVersion(@PathVariable String id, @PathVariable String versionId,
                                                             @Valid @RequestBody CodesetVersion codesetVersion) throws IOException, NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetVersion newCodesetVersion = codesetService.saveCodesetVersion(id, versionId, codesetVersion, authentication.getName());
//        return new ResponseEntity<>(newCodesetVersion, HttpStatus.OK);
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Code Set Version Saved Successfully", newCodesetVersion.getId(), null, newCodesetVersion);
    }

    @RequestMapping(value = "/{id}/versions/{versionId}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseMessage<?> deleteCodesetVersion(@PathVariable String id, @PathVariable String versionId) throws IOException, NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        codesetService.deleteCodesetVersion(id, versionId, authentication.getName());
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Code Set Version Deleted Successfully", null, null, null);
    }


    @RequestMapping(value = "/{id}/versions/{versionId}/exportCSV", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8")
    public void exportCSV(@PathVariable String id, @PathVariable String versionId, HttpServletResponse response)
            throws IOException {
        CodesetVersion codesetVersion = codesetVersionService.getVersionDetails(id,versionId);
        String csvContent = new TableCSVGenerator().generate(codesetVersion.getCodes());
        try (InputStream content = IOUtils.toInputStream(csvContent, "UTF-8")) {
            response.setContentType("text/csv");
            response.setHeader("Content-disposition", "attachment;filename=" + codesetVersion.getVersion()
                    + "-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv");
            FileCopyUtils.copy(content, response.getOutputStream());
        }
    }

    @RequestMapping(value = "/{id}/versions/{versionId}/commit", produces = "application/json", method = RequestMethod.POST)
    public ResponseMessage<?> commitCodesetVersion(@PathVariable String id, @PathVariable String versionId, @RequestBody CommitRequest body) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CodesetVersion newCodesetVersion = codesetService.commitCodesetVersion(id, versionId, body, authentication.getName());
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Code Set version Committed Successfully", newCodesetVersion.getId(), null, newCodesetVersion);
    }

    @RequestMapping(value = "/{id}/compare/{sourceVersionId}/{targetVersionId}", produces = "application/json", method = RequestMethod.GET)
    public List<CodeDelta> compare(
            @PathVariable String id,
            @PathVariable String sourceVersionId,
            @PathVariable String targetVersionId
    ) throws Exception {
        return codesetService.getCodeDelta(id, sourceVersionId, targetVersionId);
    }


}
