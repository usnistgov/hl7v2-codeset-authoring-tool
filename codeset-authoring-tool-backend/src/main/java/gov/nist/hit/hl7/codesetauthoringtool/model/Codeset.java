package gov.nist.hit.hl7.codesetauthoringtool.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "codesets")
public class Codeset {

    @Id
    private String id;
    private String name;
    private  String description;
    private Boolean disableKeyProtection;
    private Date dateUpdated;
    private Date dateCreated;
    @OneToMany(mappedBy = "codeset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodesetVersion> versions = new ArrayList<>();
    private String latestVersion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser owner;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "codeset_apikey", // Join table name
            joinColumns = @JoinColumn(name = "codeset_id"), // Foreign key in the join table for Codeset
            inverseJoinColumns = @JoinColumn(name = "apikey_id") // Foreign key in the join table for ApiKey
    )
    private Set<ApiKey> apiKeys = new HashSet<>();

    @PrePersist
    public void generateId() {
        // This will generate a UUID and remove hyphens to get a 32-character string
        this.id = UUID.randomUUID().toString().replace("-", "");
    }
    public Codeset(){}
    public Codeset(String name,String description, Boolean disableKeyProtection, Date dateUpdated, Date dateCreated, List<CodesetVersion> versions) {
        this.name = name;
        this.description = description;
        this.disableKeyProtection = disableKeyProtection == null ? false : disableKeyProtection;
        this.dateUpdated = dateUpdated;
        this.dateCreated = dateCreated;
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

    public List<CodesetVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<CodesetVersion> versions) {
        this.versions = versions;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    public Set<ApiKey> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Set<ApiKey> apiKeys) {
        this.apiKeys = apiKeys;
    }
}
