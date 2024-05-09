package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetVersionRequest;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetRepository;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetVersionRepository;
import gov.nist.hit.hl7.codesetauthoringtool.service.CodesetService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Service
public class CodesetServiceImpl implements CodesetService {
    private final CodesetRepository codesetRepository;
    private final CodesetVersionRepository codesetVersionRepository;

    public CodesetServiceImpl(CodesetRepository codesetRepository, CodesetVersionRepository codesetVersionRepository) {
        this.codesetRepository = codesetRepository;
        this.codesetVersionRepository = codesetVersionRepository;
    }

    @Override
    public Codeset createCodeset(CodesetRequest codeset) throws IOException {


        // Check if a Codeset with the same audience already exists
        if (codesetRepository.findByAudience(codeset.getAudience()).isPresent()) {
            throw new IllegalArgumentException("Codeset already exists");
        }
        Codeset newCodeset = new Codeset(
                codeset.getName(),
                codeset.getAudience(),
                codeset.getExposed(),
                new Date(),
                new ArrayList<>()
        );
        CodesetVersion newCodesetVersion = new CodesetVersion(
                "1",codeset.getExposed(), new Date(), "unpublished", new ArrayList<>(),  newCodeset
        );
        newCodeset.addVersion(newCodesetVersion);
        newCodeset.setLatestVersion(newCodesetVersion);

        return codesetRepository.save(newCodeset);
    }

    @Override
    public List<Codeset> getCodesets(CodesetSearchCriteria criteria) throws IOException {
        return codesetRepository.findAll((Specification<Codeset>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getScope() != null) {
                predicates.add(criteriaBuilder.equal(root.get("scope"), criteria.getScope()));
            }
            if (criteria.getVersion() != null) {
                predicates.add(criteriaBuilder.equal(root.get("versionNumber"), criteria.getVersion()));
            }
            if (criteria.getName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Codeset getCodeset(String id) throws IOException {
        return codesetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found with id " + id));
    }

}
