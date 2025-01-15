package gov.nist.hit.hl7.codesetauthoringtool.dto;
import java.util.Date;
import java.util.List;
public class CodesetDTO {
    private String id;
    private String name;
    private String description;
    private Boolean disableKeyProtection;
    private Date dateUpdated;
    private Date dateCreated;
    private List<CodesetVersionSimpleDTO> versions;
    private String latestVersion;

    public CodesetDTO(String id, String name, String description, Boolean disableKeyProtection, Date dateUpdated, Date dateCreated, List<CodesetVersionSimpleDTO> versions, String latestVersion) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.disableKeyProtection = disableKeyProtection;
        this.dateUpdated = dateUpdated;
        this.dateCreated = dateCreated;
        this.versions = versions;
        this.latestVersion = latestVersion;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisableKeyProtection() {
        return disableKeyProtection;
    }

    public void setDisableKeyProtection(Boolean disableKeyProtection) {
        this.disableKeyProtection = disableKeyProtection;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<CodesetVersionSimpleDTO> getVersions() {
        return versions;
    }

    public void setVersions(List<CodesetVersionSimpleDTO> versions) {
        this.versions = versions;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }
}
