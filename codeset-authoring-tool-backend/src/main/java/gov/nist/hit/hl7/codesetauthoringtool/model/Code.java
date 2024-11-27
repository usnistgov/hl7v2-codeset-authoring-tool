package gov.nist.hit.hl7.codesetauthoringtool.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "codes")
public class Code {
    @Id
    private String id;
    private String code;
    private String description;
    private String system;
    private String display;
    private String usage;
    private String comments;
    private String pattern;
    private Boolean hasPattern;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codeset_version_id")
    @JsonBackReference
    private CodesetVersion codesetVersion;

    @PrePersist
    public void generateId() {
        // This will generate a UUID and remove hyphens to get a 32-character string
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public Code(){

    }

    public Code(String code, String system, String display) {
        this.code = code;
        this.system = system;
        this.display = display;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CodesetVersion getCodesetVersion() {
        return codesetVersion;
    }

    public void setCodesetVersion(CodesetVersion codesetVersion) {
        this.codesetVersion = codesetVersion;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Boolean getHasPattern() {
        return hasPattern;
    }

    public void setHasPattern(Boolean hasPattern) {
        this.hasPattern = hasPattern;
    }
}
