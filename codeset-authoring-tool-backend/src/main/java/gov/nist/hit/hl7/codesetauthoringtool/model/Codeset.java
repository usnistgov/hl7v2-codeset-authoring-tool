package gov.nist.hit.hl7.codesetauthoringtool.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "codesets")
public class Codeset {

    @Id
    private String id;
    private String name;
    private String audience;
    private Boolean isPublic;
    private Date dateUpdated;

    @OneToMany(mappedBy = "codeset", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CodesetVersion> versions = new ArrayList<>();

    @OneToOne
    private CodesetVersion latestVersion;

    @PrePersist
    public void generateId() {
        // This will generate a UUID and remove hyphens to get a 32-character string
        this.id = UUID.randomUUID().toString().replace("-", "");
    }
    public Codeset(){

    }
    public Codeset(String name, String audience, Boolean isPublic, Date dateUpdated, List<CodesetVersion> versions) {
        this.name = name;
        this.audience = audience;
        this.isPublic = isPublic;
        this.dateUpdated = dateUpdated;
        this.versions = new ArrayList<>(versions);

    }

    public void addVersion(CodesetVersion version) {
        versions.add(version);
        version.setCodeset(this);
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

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public List<CodesetVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<CodesetVersion> versions) {
        this.versions = versions;
    }

    public CodesetVersion getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(CodesetVersion latestVersion) {
        this.latestVersion = latestVersion;
    }
}
