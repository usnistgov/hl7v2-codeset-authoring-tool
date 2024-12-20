package gov.nist.hit.hl7.codesetauthoringtool.model.request;

import gov.nist.hit.hl7.codesetauthoringtool.model.Code;

import java.util.List;

public class CommitRequest {
    private String version;
    private String comments;
    private Boolean latest;

    public CommitRequest() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }


}
