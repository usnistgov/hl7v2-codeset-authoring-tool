package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetVersionRequest;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetRepository;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetVersionRepository;
import gov.nist.hit.hl7.codesetauthoringtool.service.CodesetService;
import gov.nist.hit.hl7.codesetauthoringtool.service.CodesetVersionService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CodesetVersionServiceImpl implements CodesetVersionService {
    private final CodesetRepository codesetRepository;
    private final CodesetVersionRepository codesetVersionRepository;

    public CodesetVersionServiceImpl(CodesetRepository codesetRepository, CodesetVersionRepository codesetVersionRepository) {
        this.codesetRepository = codesetRepository;
        this.codesetVersionRepository = codesetVersionRepository;
    }

    @Override
    public CodesetVersion addVersionToCodeset(String codesetId, CodesetVersionRequest codesetVersionRequest) {
        Optional<Codeset> codesetOptional = codesetRepository.findById(codesetId);
        if (!codesetOptional.isPresent()) {
            throw new IllegalArgumentException("Codeset not found with ID: " + codesetId);
        }
        Codeset codeset = codesetOptional.get();
        Optional<CodesetVersion> existingCodesetVersion = codesetVersionRepository.findByCodesetIdAndVersion(codeset.getId(),codesetVersionRequest.getVersion());
        if(existingCodesetVersion.isPresent()){
            throw new IllegalArgumentException("Version " + codesetVersionRequest.getVersion() + " already exists for this Codeset");
        }
        CodesetVersion newCodesetVersion = new CodesetVersion(
                codesetVersionRequest.getVersion(),codesetVersionRequest.getExposed(), new Date(), "unpublished", new ArrayList<>(),  codeset
        );
        codesetVersionRepository.save(newCodesetVersion);
        return  newCodesetVersion;

    }

    @Override
    public CodesetVersion getVersionDetails(String codesetId, String version) {
        Optional<Codeset> codesetOptional = codesetRepository.findById(codesetId);
        if (!codesetOptional.isPresent()) {
            throw new IllegalArgumentException("Codeset not found with ID: " + codesetId);
        }
        Optional<CodesetVersion> codesetVersion = codesetVersionRepository.findByCodesetIdAndVersion(version);
        if(!codesetVersion.isPresent()){
            throw new IllegalArgumentException("Version " + version+ " doesn't exist for this Codeset");
        }
        return  codesetVersion.get();

    }
}
