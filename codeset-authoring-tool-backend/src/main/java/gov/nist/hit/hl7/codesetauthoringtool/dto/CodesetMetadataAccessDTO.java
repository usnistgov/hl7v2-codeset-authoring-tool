package gov.nist.hit.hl7.codesetauthoringtool.dto;
import java.util.List;

public class CodesetMetadataAccessDTO {
    private String id;
    private String name;
    private VersionAccessDTO latestStableVersion;
    private List<VersionAccessDTO> versions;


    public CodesetMetadataAccessDTO() {
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

    public List<VersionAccessDTO> getVersions() {
        return versions;
    }

    public void setVersions(List<VersionAccessDTO> versions) {
        this.versions = versions;
    }
}
