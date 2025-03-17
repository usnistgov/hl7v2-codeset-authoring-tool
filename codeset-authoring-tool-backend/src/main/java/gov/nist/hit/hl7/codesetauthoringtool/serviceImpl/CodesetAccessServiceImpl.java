package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.dto.*;
import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.exception.ResourceAPIAccessDeniedException;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.Code;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetRepository;
import gov.nist.hit.hl7.codesetauthoringtool.repository.CodesetVersionRepository;
import gov.nist.hit.hl7.codesetauthoringtool.service.CodesetAccessService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodesetAccessServiceImpl implements CodesetAccessService {
    private final CodesetRepository codesetRepository;
    private final CodesetVersionRepository codesetVersionRepository;

    public CodesetAccessServiceImpl(CodesetRepository codesetRepository, CodesetVersionRepository codesetVersionRepository) {
        this.codesetRepository = codesetRepository;
        this.codesetVersionRepository = codesetVersionRepository;
    }

    @Override
    public CodesetAccessDTO getCodeset(String id, String version, String match, String apiKey) throws ResponseStatusException, NotFoundException, ResourceAPIAccessDeniedException {
        Codeset codeset = codesetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CodeSet with id " + id + " not found"));
        if(!codeset.getDisableKeyProtection() && apiKey == null) {
            throw new ResourceAPIAccessDeniedException( "Missing API key");
        }
        if(!codeset.getDisableKeyProtection()){
            String hashedApiKey = DigestUtils.sha256Hex(apiKey);
            ApiKey matchedKey = codeset.getApiKeys().stream().filter((c) -> c.getToken().equals(hashedApiKey)).findFirst().orElseThrow(()-> new ResourceAPIAccessDeniedException("Codeset not accessible "));
        }
        CodesetVersion targetVersion;
        CodesetVersion latestVersion = codeset.getVersions().stream().filter(v -> v.getVersion().equals(codeset.getLatestVersion())).findFirst().orElseThrow(()-> new NotFoundException( "CodeSet id " + id + " has no latest version"));
        if(version != null && !version.isEmpty()){
            targetVersion = codeset.getVersions().stream().filter(v -> v.getVersion().equals(version)).findFirst().orElseThrow(()-> new NotFoundException("CodeSet id " + id + " with version " + version + " not found"));
        } else if(codeset.getLatestVersion() != null){
            targetVersion = latestVersion;
        } else {
            throw new NotFoundException( "Error while trying to determine latest version of code set id "+ id);
        }

        CodesetVersion codesetVersion = codesetVersionRepository.findWithCodesById(targetVersion.getId())
                .orElseThrow(() -> new NotFoundException("Codeset version not found or you don't have access to it."));
        List<Code> filteredCodes = new ArrayList<>();
        // Query the database for the matching codes using the repository
        if(match != null && !match.isEmpty()){
            List<Object[]> temp = codesetVersionRepository.findCodesByVersionIdAndMatch(targetVersion.getId(), match);
            filteredCodes = temp.stream()
                    .map(obj -> new Code(
                            (String) obj[0],
                            (String) obj[1],
                            (String) obj[4],
                            (String) obj[6],
                            (String) obj[3],
                            (String) obj[5],
                            (Boolean) obj[2]

                    ))
                    .collect(Collectors.toList());
        } else {
            filteredCodes = codesetVersion.getCodes();
        }

        CodesetAccessDTO result = new CodesetAccessDTO();
        result.setId(codeset.getId());
        result.setName(codeset.getName());
        result.setLatestStableVersion(new VersionAccessDTO(latestVersion.getVersion(), latestVersion.getDateCommitted()));
        result.setVersion(new VersionAccessDTO(targetVersion.getVersion(), targetVersion.getDateCommitted()));
        result.setCodeMatchValue(match);
        result.setCodes(convertCodesToDTO(filteredCodes));
        result.setLatestStable(targetVersion.getVersion().equals(latestVersion.getVersion()));
        return result;
    }

    @Override
    public CodesetMetadataAccessDTO getCodesetMetadata(String id, String apiKey) throws ResponseStatusException, ResourceAPIAccessDeniedException, NotFoundException {
        Codeset codeset = codesetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CodeSet with id " + id + " not found"));
        if(!codeset.getDisableKeyProtection() && apiKey == null) {
            throw new ResourceAPIAccessDeniedException( "Missing API key");
        }
        if(!codeset.getDisableKeyProtection()){
            String hashedApiKey = DigestUtils.sha256Hex(apiKey);
            ApiKey matchedKey = codeset.getApiKeys().stream().filter((c) -> c.getToken().equals(hashedApiKey)).findFirst().orElseThrow(()-> new ResourceAPIAccessDeniedException("Codeset not accessible "));
        }

        return convertCodesetToMetadataDTO(codeset);

    }
    @Override
    public CodesetVersionMetadataAccessDTO getCodesetVersionMetadata(String id, String version, String apiKey) throws   NotFoundException, ResourceAPIAccessDeniedException {
        Codeset codeset = codesetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CodeSet with id " + id + " not found"));
        if(!codeset.getDisableKeyProtection() && apiKey == null) {
            throw new ResourceAPIAccessDeniedException( "Missing API key");
        }
        if(!codeset.getDisableKeyProtection()){
            String hashedApiKey = DigestUtils.sha256Hex(apiKey);
            ApiKey matchedKey = codeset.getApiKeys().stream().filter((c) -> c.getToken().equals(hashedApiKey)).findFirst().orElseThrow(()-> new ResourceAPIAccessDeniedException("Codeset not accessible "));
        }
        CodesetVersion codesetVersion = codeset.getVersions().stream().filter(v -> v.getVersion().equals(version)).findFirst().orElseThrow(()-> new NotFoundException("Codeset version not found "));
        CodesetVersionMetadataAccessDTO result = new CodesetVersionMetadataAccessDTO();
        result.setId(id);
        result.setVersion(version);
        result.setName(codeset.getName());
        result.setDate(codesetVersion.getDateCommitted());
        result.setNumberOfCodes(codesetVersion.getCodes().size());
        return result;

    }
    public String getKeyHash(String key) {
        return DigestUtils.sha256Hex(key);
    }
    List<CodeAccessDTO> convertCodesToDTO(List<Code> codes){
        return  codes.stream().map(c -> new CodeAccessDTO(c.getCode(), c.getSystem(), c.getDescription(), c.getPattern(), c.getUsage(), c.getHasPattern())).toList();
    }

    CodesetMetadataAccessDTO convertCodesetToMetadataDTO(Codeset codeset) throws  NotFoundException {
        CodesetMetadataAccessDTO result = new CodesetMetadataAccessDTO();
        CodesetVersion latestVersion = codeset.getVersions().stream().filter(v -> v.getVersion().equals(codeset.getLatestVersion())).findFirst().orElseThrow(()-> new NotFoundException( "CodeSet id " + codeset.getId() + " has no latest version"));
        List<VersionAccessDTO> codesetVersionsDTO = convertVersionsToDTO(codeset.getVersions());
        result.setId(codeset.getId());
        result.setName(codeset.getName());
        result.setLatestStableVersion(new VersionAccessDTO(latestVersion.getVersion(), latestVersion.getDateCommitted()));
        result.setVersions(codesetVersionsDTO);
        return result;
    }
    List<VersionAccessDTO> convertVersionsToDTO(List<CodesetVersion> versions){
        return versions.stream().filter(v -> v.getDateCommitted() != null)
                .map(c -> new VersionAccessDTO(c.getVersion(), c.getDateCommitted()))
                .collect(Collectors.toList());
    }
}
