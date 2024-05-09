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
    private String system;
    private String display;

    @ManyToOne
    @JoinColumn(name = "id", insertable=false, updatable=false)
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
}
