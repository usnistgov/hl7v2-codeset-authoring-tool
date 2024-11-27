package gov.nist.hit.hl7.codesetauthoringtool.dto;
import java.util.Date;

public class CodesetVersionSimpleDTO {
    private String id;
    private String version;
    private Boolean exposed;
    private Date dateCreated;
    private Date dateCommitted;
    private String status;
    private String comments;

    public CodesetVersionSimpleDTO(String id, String version, Boolean exposed, Date dateCreated,Date dateCommitted, String status, String comments) {
        this.id = id;
        this.version = version;
        this.exposed = exposed;
        this.dateCreated = dateCreated;
        this.dateCommitted = dateCommitted;
        this.status = status;
        this.comments = comments;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
