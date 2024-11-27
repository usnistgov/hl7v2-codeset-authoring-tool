package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetListItemDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetVersionSimpleDTO;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApplicationUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.Code;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CommitRequest;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetRepository;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetVersionRepository;
import gov.nist.hit.hl7.codesetauthoringtool.repository.UserRepository;
import gov.nist.hit.hl7.codesetauthoringtool.service.CodesetService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CodesetServiceImpl implements CodesetService {
    private final CodesetRepository codesetRepository;

    private final UserRepository userRepository;
    private final CodesetVersionRepository codesetVersionRepository;

    public CodesetServiceImpl(CodesetRepository codesetRepository, UserRepository userRepository, CodesetVersionRepository codesetVersionRepository) {
        this.codesetRepository = codesetRepository;
        this.userRepository = userRepository;
        this.codesetVersionRepository = codesetVersionRepository;
    }

    @Override
    @Transactional
    public Codeset createCodeset(CodesetRequest codeset, String username) throws IOException {
        if (codesetRepository.findByName(codeset.getName()).isPresent()) {
            throw new IOException("Codeset already exists");
        }
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Codeset newCodeset = new Codeset(
                codeset.getName(),
                codeset.getDescription(),
                codeset.getExposed(),
                new Date(),
                new Date(),
                new ArrayList<>()
        );
        CodesetVersion newCodesetVersion = new CodesetVersion(
                "1", codeset.getExposed(), new Date(), "unpublished", new ArrayList<>(), newCodeset
        );
        if(codeset.getCodes() != null){
            for (Code code : codeset.getCodes()) {
                code.setCodesetVersion(newCodesetVersion);
                newCodesetVersion.getCodes().add(code);
            }
        }

        newCodeset.addVersion(newCodesetVersion);
        newCodeset.setOwner(owner);
        return codesetRepository.save(newCodeset);
    }
    @Override
    @Transactional
    public CodesetDTO updateCodeset(String id, CodesetRequest codeset, String username) throws IOException {
        Codeset existingCodeset = codesetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));

        existingCodeset.setName(codeset.getName());
        existingCodeset.setDescription(codeset.getDescription());
        existingCodeset.setDateUpdated(new Date());
        existingCodeset.setPublic(codeset.getExposed());

        Codeset updatedCodeset =  codesetRepository.save(existingCodeset);
        return  convertToDTO(updatedCodeset);
    }


    @Override
    public List<CodesetDTO> getCodesets(CodesetSearchCriteria criteria, String username) throws IOException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<Codeset> codesets;
        if (criteria.getName() == null || criteria.getName().isEmpty()) {
            codesets = codesetRepository.findAll();
        } else {
            codesets = codesetRepository.findByNameContaining(criteria.getName());
        }

        return codesets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<CodesetListItemDTO> getCodesetsList(CodesetSearchCriteria criteria, String username) throws IOException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<Codeset> codesets;
        if (criteria.getName() == null || criteria.getName().isEmpty()) {
            codesets = codesetRepository.findAll();
        } else {
            codesets = codesetRepository.findByNameContaining( criteria.getName());
        }

        return codesets.stream()
                .map(this::convertToListItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CodesetDTO getCodeset(String id, String username) throws IOException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Codeset codeset = codesetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));

        return convertToDTO(codeset);
    }
    @Override
    public CodesetVersion getCodesetVersion(String id, String versionId, String username) throws IOException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        CodesetVersion codesetVersion = codesetVersionRepository.findWithCodesById(versionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));
        return codesetVersion;
    }

    @Override
    public CodesetVersion saveCodesetVersion(String codesetId, String codesetVersionId, CodesetVersion codesetVersion, String username) throws IOException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Retrieve the existing CodesetVersion from the database
        CodesetVersion existingCodesetVersion = this.codesetVersionRepository.findById(codesetVersionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset version not found."));

        // Clear the existing codes
        existingCodesetVersion.getCodes().clear();

        // Set the updated properties
        existingCodesetVersion.setExposed(codesetVersion.getExposed());

        // Add the new codes and set the relationship properly
        for (Code code : codesetVersion.getCodes()) {
            code.setCodesetVersion(existingCodesetVersion); // Ensure the correct association
            existingCodesetVersion.getCodes().add(code);
        }

        // Save the updated CodesetVersion
        return codesetVersionRepository.save(existingCodesetVersion);
    }

    @Override
    @Transactional
    public CodesetVersion commitCodesetVersion(String codesetId, String codesetVersionId, CommitRequest body, String username) throws IOException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Retrieve the current Codeset and its latest version
        Codeset codeset = codesetRepository.findById(codesetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));

        CodesetVersion existingCodesetVersion = this.codesetVersionRepository.findById(codesetVersionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset version not found."));

        if(existingCodesetVersion.getDateCommitted() != null){
           throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codeset version already committed.");
        }
        existingCodesetVersion.setDateCommitted(new Date());
        existingCodesetVersion.setStatus("published");
        existingCodesetVersion.setComments(body.getComments());

        // Clear the existing codes
        existingCodesetVersion.getCodes().clear();
        // Add the new codes and set the relationship properly
        for (Code code : body.getCodes()) {
            code.setCodesetVersion(existingCodesetVersion); // Ensure the correct association
            existingCodesetVersion.getCodes().add(code);
        }

        // Increment the version number
        String newVersionNumber = String.valueOf(Integer.parseInt(existingCodesetVersion.getVersion()) + 1);

        // Create a new CodesetVersion
        CodesetVersion newCodesetVersion = new CodesetVersion(
                newVersionNumber, existingCodesetVersion.getExposed(), new Date(), "unpublished", new ArrayList<>(), codeset
        );

        // Duplicate the codes from the latest version
        for (Code code : existingCodesetVersion.getCodes()) {
            Code newCode = new Code();
            newCode.setCode(code.getCode());
            newCode.setCodesetVersion(newCodesetVersion);
            newCode.setHasPattern(code.getHasPattern());
            newCode.setPattern(code.getPattern());
            newCode.setDescription(code.getDescription());
            newCode.setSystem(code.getSystem());
            newCode.setUsage(code.getUsage());
            newCode.setComments(code.getComments());
            newCodesetVersion.getCodes().add(newCode);
        }
        existingCodesetVersion.setVersion(body.getVersion());

        // Add the new version to the Codeset
        codeset.getVersions().add(newCodesetVersion);
        codeset.setLatestVersion(existingCodesetVersion.getVersion());
        codeset.setDateUpdated(new Date());
        // Save the Codeset with the new version
        codesetRepository.save(codeset);
        codesetVersionRepository.save(existingCodesetVersion);
        return existingCodesetVersion;
    }

    private CodesetDTO convertToDTO(Codeset codeset) {
        List<CodesetVersionSimpleDTO> versions = codeset.getVersions().stream()
                .map(version -> new CodesetVersionSimpleDTO(
                        version.getId(),
                        version.getVersion(),
                        version.getExposed(),
                        version.getDateCreated(),
                        version.getDateCommitted(),
                        version.getStatus(),
                        version.getComments()
                )).sorted((v1, v2) -> v2.getDateCreated().compareTo(v1.getDateCreated()))
                .collect(Collectors.toList());

        return new CodesetDTO(
                codeset.getId(),
                codeset.getName(),
                codeset.getDescription(),
                codeset.getPublic(),
                codeset.getDateUpdated(),
                codeset.getDateCreated(),
                versions,
                codeset.getLatestVersion()
        );
    }
    private CodesetListItemDTO convertToListItemDTO(Codeset codeset) {
        return new CodesetListItemDTO(
                codeset.getId(),
                codeset.getName()
        );
    }
}
