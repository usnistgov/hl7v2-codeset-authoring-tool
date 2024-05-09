package gov.nist.hit.hl7.codesetauthoringtool.model.request;


import gov.nist.hit.hl7.codesetauthoringtool.model.Code;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CodesetRequest {
    private String name;
    private String audience;
    private String version;
    private Boolean exposed;

    private List<Code> codes;

    // Constructors, Getters, and Setters

    public CodesetRequest() {
    }

    public CodesetRequest(String name,String audience, String version, Boolean exposed, List<Code> codes) {
        this.name = name;
        this.audience = audience;
        this.version = version;
        this.exposed = exposed;
        this.codes = codes;
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
}
