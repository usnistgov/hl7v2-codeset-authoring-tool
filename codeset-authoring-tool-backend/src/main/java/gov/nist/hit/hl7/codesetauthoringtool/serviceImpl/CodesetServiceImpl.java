package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetListItemDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetVersionSimpleDTO;
import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.model.*;
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
                codeset.getDisableKeyProtection(),
                new Date(),
                new Date(),
                new ArrayList<>()
        );
        CodesetVersion newCodesetVersion = new CodesetVersion(
                "", codeset.getDisableKeyProtection(), new Date(), "unpublished", new ArrayList<>(), newCodeset
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
        existingCodeset.setDisableKeyProtection(codeset.getDisableKeyProtection());
        existingCodeset.setLatestVersion(codeset.getLatestVersion());
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
    public void deleteCodeset(String id, String username) throws IOException, NotFoundException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));

        Codeset codeset = codesetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Codeset not found or you don't have access to it."));
        this.codesetRepository.deleteById(id);
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
    public CodesetVersion saveCodesetVersion(String codesetId, String codesetVersionId, CodesetVersion codesetVersion, String username) throws IOException, NotFoundException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Retrieve the existing CodesetVersion from the database
        CodesetVersion existingCodesetVersion = this.codesetVersionRepository.findById(codesetVersionId)
                .orElseThrow(() -> new NotFoundException("Codeset version not found."));

        // Clear the existing codes
        existingCodesetVersion.getCodes().clear();

        // Set the updated properties
        existingCodesetVersion.setDisableKeyProtection(codesetVersion.getDisableKeyProtection());

        // Add the new codes and set the relationship properly
        for (Code code : codesetVersion.getCodes()) {
            code.setCodesetVersion(existingCodesetVersion); // Ensure the correct association
            existingCodesetVersion.getCodes().add(code);
        }
        System.out.println(existingCodesetVersion.getCodes());
        // Save the updated CodesetVersion
        return codesetVersionRepository.save(existingCodesetVersion);
    }
    @Override
    public void deleteCodesetVersion(String codesetId, String codesetVersionId,String username) throws IOException, NotFoundException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));

        Codeset codeset = codesetRepository.findById(codesetId)
                .orElseThrow(() -> new NotFoundException("Codeset not found or you don't have access to it."));

        if(!codeset.getVersions().stream().filter(v -> codesetVersionId.equals(v.getId())).findAny().isPresent()) {
            throw new NotFoundException("Codeset version not found.");
        }

        // Retrieve the existing CodesetVersion from the database
        CodesetVersion existingCodesetVersion = this.codesetVersionRepository.findById(codesetVersionId)
                .orElseThrow(() -> new NotFoundException("Codeset version not found."));

        if(existingCodesetVersion.getDateCommitted() == null) {
            throw new IOException( "Cannot delete the version in progress.");
        }
        if(existingCodesetVersion.getVersion().equals(codeset.getLatestVersion())) {
            throw new IOException( "Cannot delete the version marked as latest.");
        }

        codeset.getVersions().remove(existingCodesetVersion);

        this.codesetRepository.save(codeset);
        this.codesetVersionRepository.deleteById(existingCodesetVersion.getId());

    }

    @Override
    @Transactional
    public CodesetVersion commitCodesetVersion(String codesetId, String codesetVersionId, CommitRequest body, String username) throws IOException {
        ApplicationUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if(body.getVersion() == null) {
            throw new IOException("CodeSet Version is required.");
        }
        String cleanedVersion = body.getVersion().trim().toLowerCase();
        if(cleanedVersion.isEmpty()) {
            throw new IOException("CodeSet Version is required.");
        }

        // Retrieve the current Codeset
        Codeset codeset = codesetRepository.findById(codesetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));

        CodesetVersion existingCodesetVersion = this.codesetVersionRepository.findById(codesetVersionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset version not found."));


        if(existingCodesetVersion.getDateCommitted() != null){
           throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codeset version already committed.");
        }
        List<CodesetVersion> versions = codesetVersionRepository.findByCodesetId(codesetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));

        CodesetVersion duplicate = versions.stream().filter((version) -> version.getDateCommitted() != null && version.getVersion().equals(cleanedVersion))
                .findFirst()
                .orElse(null);
        if(duplicate != null) {
            throw new IOException("CodeSet Version number "+cleanedVersion+" already exists.");
        }

        existingCodesetVersion.setDateCommitted(new Date());
        existingCodesetVersion.setStatus("published");
        existingCodesetVersion.setComments(body.getComments());



        // Create a new CodesetVersion
        CodesetVersion newCodesetVersion = new CodesetVersion(
                "", existingCodesetVersion.getDisableKeyProtection(), new Date(), "unpublished", new ArrayList<>(), codeset
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
        existingCodesetVersion.setVersion(cleanedVersion);

        // Add the new version to the Codeset
        codeset.getVersions().add(newCodesetVersion);
        if(body.getLatest()){
            codeset.setLatestVersion(cleanedVersion);
        }
        codeset.setDateUpdated(new Date());
        // Save the Codeset with the new version
        codesetRepository.save(codeset);
        codesetVersionRepository.save(existingCodesetVersion);
        return existingCodesetVersion;
    }

    @Override
    public List<CodeDelta> getCodeDelta(String codesetId, String codeSetVersionId, String targetId) throws Exception {
        // Retrieve the current Codeset and its latest version
        Codeset codeset = codesetRepository.findById(codesetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));
        List<CodesetVersion> versions = codesetVersionRepository.findByCodesetId(codesetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset not found or you don't have access to it."));
        CodesetVersion sourceVersion = versions.stream().filter((v) -> v.getId().equals(codeSetVersionId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source version not found"));
        CodesetVersion targetVersion = versions.stream().filter((v) -> v.getId().equals(targetId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target version not found"));

        if(targetVersion.getDateCommitted() == null) {
            throw new Exception("You can only compare against a committed version");
        }
        if(sourceVersion.getDateCommitted() != null && sourceVersion.getDateCommitted().before(targetVersion.getDateCommitted())) {
            throw new Exception("You can only compare against a previous version");
        }
//        CodeSetVersion source = findCodeSetVersionById(sourceVersion.getId());
//        CodeSetVersion target = findCodeSetVersionById(targetVersion.getId());
        return compareCodes(sourceVersion.getCodes(), targetVersion.getCodes());
    }
    public List<CodeDelta> compareCodes(List<Code> source, List<Code> target) {
        List<CodeDelta> codes = new ArrayList<>();
        List<Code> targetCopy = new ArrayList<>(target);
        for(Code code: source) {
            Code compareTo = findCandidate(targetCopy, code);
            if(compareTo != null) {
                targetCopy.remove(compareTo);
            }
            codes.add(compare(code, compareTo));
        }
        for(Code code: targetCopy) {
            codes.add(compare(null, code));
        }
        return codes;
    }
    public CodeDelta compare(Code source, Code target) {
        CodeDelta delta = new CodeDelta();
        delta.setChange(DeltaChange.NONE);

        // Value
        String sourceValue = source != null ? source.getCode() : null;
        String targetValue = target != null ? target.getCode() : null;
        PropertyDelta<String> value = compareStringValues(sourceValue, targetValue);
        delta.setValue(value);
        setChangeType(delta, value);

        // Code System
        String sourceCodeSystem = source != null ? source.getSystem() : null;
        String targetCodeSystem = target != null ? target.getSystem() : null;
        PropertyDelta<String> codeSystem = compareStringValues(sourceCodeSystem, targetCodeSystem);
        delta.setCodeSystem(codeSystem);
        setChangeType(delta, codeSystem);

        // Description
        String sourceDesc = source != null ? source.getDescription() : null;
        String targetDesc = target != null ? target.getDescription() : null;
        PropertyDelta<String> description = compareStringValues(sourceDesc, targetDesc);
        delta.setDescription(description);
        setChangeType(delta, description);

        // Comments
        String sourceComments = source != null ? source.getComments() : null;
        String targetComments = target != null ? target.getComments() : null;
        PropertyDelta<String> comments = compareStringValues(sourceComments, targetComments);
        delta.setComments(comments);
        setChangeType(delta, comments);

        // Usage
        String sourceUsage = source != null ? source.getUsage() : null;
        String targetUsage = target != null ? target.getUsage() : null;
        PropertyDelta<String> usage = compareStringValues(sourceUsage, targetUsage);
        delta.setUsage(usage);
        setChangeType(delta, usage);

        // Has Pattern
        Boolean sourceHasPattern = source != null ? source.getHasPattern() : null;
        Boolean targetHasPattern = target != null ? target.getHasPattern() : null;
        PropertyDelta<Boolean> hasPattern = compareBooleanValues(sourceHasPattern, targetHasPattern);
        delta.setHasPattern(hasPattern);
        setChangeType(delta, hasPattern);

        // Pattern
        String sourcePattern = source != null ? source.getPattern() : null;
        String targetPattern = target != null ? target.getPattern() : null;
        PropertyDelta<String> pattern = compareStringValues(sourcePattern, targetPattern);
        delta.setPattern(pattern);
        setChangeType(delta, pattern);

        if(source == null && target != null) {
            delta.setChange(DeltaChange.DELETED);
        } else if(target == null && source != null) {
            delta.setChange(DeltaChange.ADDED);
        }

        return delta;
    }
    public void setChangeType(CodeDelta delta, PropertyDelta propertyDelta) {
        if(!propertyDelta.getChange().equals(DeltaChange.NONE)) {
            delta.setChange(DeltaChange.CHANGED);
        }
    }
    public Code findCandidate(List<Code> list, Code code) {
        List<Code> codeMatch = list.stream().filter((candidate) -> !(code.getCode() == null || code.getCode().isEmpty()) && !(candidate.getCode() == null || candidate.getCode().isEmpty()) && code.getCode().equals(candidate.getCode()))
                .collect(Collectors.toList());
        if(codeMatch.size() == 1) {
            return codeMatch.get(0);
        }
        List<Code> codeSystemMatch = codeMatch.stream().filter((candidate) -> !(code.getSystem() == null || code.getSystem().isEmpty()) && !(candidate.getSystem() == null || candidate.getSystem().isEmpty()) && code.getSystem().equals(candidate.getSystem()))
                .collect(Collectors.toList());
        if(codeSystemMatch.size() == 1) {
            return codeSystemMatch.get(0);
        }
        List<Code> idMatch = codeMatch.stream().filter((candidate) -> !(code.getId() == null || code.getId().isEmpty()) && !(candidate.getId() == null || candidate.getId().isEmpty()) && code.getId().equals(candidate.getId()))
                .collect(Collectors.toList());
        if(idMatch.size() == 1) {
            return idMatch.get(0);
        } else if(codeSystemMatch.size() > 1) {
            return codeSystemMatch.get(0);
        } else if(codeMatch.size() > 1) {
            return codeMatch.get(0);
        } else {
            return null;
        }
    }
    public PropertyDelta<Boolean> compareBooleanValues(Boolean source, Boolean target) {
        PropertyDelta<Boolean> delta = new PropertyDelta<>(source, target);
        if((truthy(target) && truthy(source)) || (falsy(target) && falsy(source))) {
            delta.setChange(DeltaChange.NONE);
        } else {
            delta.setChange(DeltaChange.CHANGED);
        }
        return delta;
    }
    public PropertyDelta<String> compareStringValues(String source, String target) {
        PropertyDelta<String> delta = new PropertyDelta<>(source, target);
        if((target == null || target.isEmpty()) && !(source == null || source.isEmpty())) {
            delta.setChange(DeltaChange.ADDED);
        } else if(!(target == null || target.isEmpty()) && (source == null || source.isEmpty())) {
            delta.setChange(DeltaChange.DELETED);
        } else if((source == null || source.isEmpty()) && (target == null || target.isEmpty())) {
            delta.setChange(DeltaChange.NONE);
        } else if(source.equals(target)) {
            delta.setChange(DeltaChange.NONE);
        } else {
            delta.setChange(DeltaChange.CHANGED);
        }
        return delta;
    }
    public boolean truthy(Boolean b) {
        return b != null && b;
    }

    public boolean falsy(Boolean b) {
        return b == null || !b;
    }

    private CodesetDTO convertToDTO(Codeset codeset) {
        List<CodesetVersionSimpleDTO> versions = codeset.getVersions().stream()
                .map(version -> new CodesetVersionSimpleDTO(
                        version.getId(),
                        version.getVersion(),
                        version.getDisableKeyProtection(),
                        version.getDateCreated(),
                        version.getDateCommitted(),
                        version.getStatus(),
                        version.getComments(),
                        (codeset.getLatestVersion() != null && codeset.getLatestVersion().equals(version.getVersion()) )? true : false
                )).sorted((v1, v2) -> v2.getDateCreated().compareTo(v1.getDateCreated()))
                .collect(Collectors.toList());

        return new CodesetDTO(
                codeset.getId(),
                codeset.getName(),
                codeset.getDescription(),
                codeset.getDisableKeyProtection(),
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
