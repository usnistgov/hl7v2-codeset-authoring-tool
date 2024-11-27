package gov.nist.hit.hl7.codesetauthoringtool.dto;

import gov.nist.hit.hl7.codesetauthoringtool.model.Code;

import java.util.Date;
import java.util.List;

public class CodesetVersionDTO {

    private String id;
    private String version;
    private Boolean exposed;
    private Date dateCreated;
    private Date dateCommitted;
    private String status;
    private List<Code> codes;

    public CodesetVersionDTO() {
    }

    public CodesetVersionDTO(String id, String version, Boolean exposed, Date dateCreated,Date dateCommitted, String status, List<Code> codes) {
        this.id = id;
        this.version = version;
        this.exposed = exposed;
        this.dateCreated = dateCreated;
        this.dateCommitted = dateCommitted;
        this.status = status;
        this.codes = codes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getExposed() {
        return exposed;
    }

    public void setExposed(Boolean exposed) {
        this.exposed = exposed;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCommitted() {
        return dateCommitted;
    }

    public void setDateCommitted(Date dateCommitted) {
        this.dateCommitted = dateCommitted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }
}
