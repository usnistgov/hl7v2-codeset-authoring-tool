package gov.nist.hit.hl7.codesetauthoringtool.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "apikeys")
public class ApiKey {

    @Id
    private String id;

    @Column(unique = true)
    private String value;
    private String label;
    private Date expirationDate;
    private String createdBy;

    // Standard constructors, getters, and setters below

    public ApiKey() {
    }

    public ApiKey(String value, String label, Date expirationDate, String createdBy) {
        this.value = value;
        this.label = label;
        this.expirationDate = expirationDate;
        this.createdBy = createdBy;
    }

    @PrePersist
    public void generateId() {
        // This will generate a UUID and remove hyphens to get a 32-character string
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
