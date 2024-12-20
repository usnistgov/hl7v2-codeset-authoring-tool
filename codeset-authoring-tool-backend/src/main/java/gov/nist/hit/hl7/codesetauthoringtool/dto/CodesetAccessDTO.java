package gov.nist.hit.hl7.codesetauthoringtool.dto;
import java.util.Date;
import java.util.List;

public class CodesetAccessDTO {
    private String id;
    private String name;
    private VersionAccessDTO latestStableVersion;
    private VersionAccessDTO version;
    private String codeMatchValue;
    private List<CodeAccessDTO> codes;
    private boolean latestStable;

    public CodesetAccessDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VersionAccessDTO getLatestStableVersion() {
        return latestStableVersion;
    }

    public void setLatestStableVersion(VersionAccessDTO latestStableVersion) {
        this.latestStableVersion = latestStableVersion;
    }

    public VersionAccessDTO getVersion() {
        return version;
    }

    public void setVersion(VersionAccessDTO version) {
        this.version = version;
    }

    public String getCodeMatchValue() {
        return codeMatchValue;
    }

    public void setCodeMatchValue(String codeMatchValue) {
        this.codeMatchValue = codeMatchValue;
    }

    public List<CodeAccessDTO> getCodes() {
        return codes;
    }

    public void setCodes(List<CodeAccessDTO> codes) {
        this.codes = codes;
    }

    public boolean isLatestStable() {
        return latestStable;
    }

    public void setLatestStable(boolean latestStable) {
        this.latestStable = latestStable;
    }
}
