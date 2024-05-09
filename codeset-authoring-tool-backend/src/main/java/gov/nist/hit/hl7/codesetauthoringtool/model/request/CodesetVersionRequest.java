package gov.nist.hit.hl7.codesetauthoringtool.model.request;

import java.util.Date;

public class CodesetVersionRequest {
    private String version;
    private Boolean exposed;
    private String status;

    public CodesetVersionRequest() {
    }

    public CodesetVersionRequest(String version, Boolean exposed, String status) {
        this.version = version;
        this.exposed = exposed;
        this.status = status;
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

    public void setExposed(Boolean aExposed) {
        exposed = aExposed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
