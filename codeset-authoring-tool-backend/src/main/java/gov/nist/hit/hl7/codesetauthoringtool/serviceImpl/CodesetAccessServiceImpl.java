package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodeAccessDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetAccessDTO;
import gov.nist.hit.hl7.codesetauthoringtool.dto.VersionAccessDTO;
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

import java.io.IOException;
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
    public CodesetAccessDTO getCodeset(String id, String version, String match, String apiKey) throws IOException, ResponseStatusException {
        Codeset codeset = codesetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CodeSet with id " + id + " not found"));
        if(!codeset.getPublic() && apiKey == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing API key");
        }
        String hashedApiKey = DigestUtils.sha256Hex(apiKey);
        System.out.println(codeset.getApiKeys());


        ApiKey matchedKey = codeset.getApiKeys().stream().filter((c) -> c.getToken().equals(hashedApiKey)).findFirst().orElseThrow(()-> new IOException("Codeset not accessible "));;

        CodesetVersion targetVersion;
        CodesetVersion latestVersion = codeset.getVersions().stream().filter(v -> v.getVersion().equals(codeset.getLatestVersion())).findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "CodeSet id " + id + " has no latest version"));
        if(version != null && !version.isEmpty()){
            targetVersion = codeset.getVersions().stream().filter(v -> v.getVersion().equals(version)).findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "CodeSet id " + id + " with version " + version + " not found"));
        } else if(codeset.getLatestVersion() != null){
            targetVersion = latestVersion;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while trying to determine latest version of code set id "+ id);
        }

        CodesetVersion codesetVersion = codesetVersionRepository.findWithCodesById(targetVersion.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeset version not found or you don't have access to it."));
        List<Code> filteredCodes = new ArrayList<>();
        // Query the database for the matching codes using the repository
        if(match != null && !match.isEmpty()){
            filteredCodes = codesetVersionRepository.findCodesByVersionIdAndMatch(targetVersion.getId(), match);
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

    public String getKeyHash(String key) {
        return DigestUtils.sha256Hex(key);
    }
    List<CodeAccessDTO> convertCodesToDTO(List<Code> codes){
        return  codes.stream().map(c -> new CodeAccessDTO(c.getCode(), c.getSystem(), c.getDescription(), c.getPattern(), c.getUsage(), c.getHasPattern())).toList();
    }
}
