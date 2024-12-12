package gov.nist.hit.hl7.codesetauthoringtool.model.request;


import gov.nist.hit.hl7.codesetauthoringtool.model.Code;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CodesetRequest {
    private String name;
    private String description;
    private String version;
    private Boolean exposed;
    private List<Code> codes;

    private String latestVersion;
    public CodesetRequest() {
    }

    public CodesetRequest(String name, String description, String version, Boolean exposed, List<Code> codes, String latestVersion) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.exposed = exposed;
        this.codes = codes;
        this.latestVersion = latestVersion;
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

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }
}
