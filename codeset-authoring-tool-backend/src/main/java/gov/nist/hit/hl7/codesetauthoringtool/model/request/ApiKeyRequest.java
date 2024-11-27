package gov.nist.hit.hl7.codesetauthoringtool.model.request;


import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApiKeyRequest {
    Set<Codeset> codesets;
    boolean expires;
    int validityDays;
    String name;

    public ApiKeyRequest() {
    }

    public Set<Codeset> getCodesets() {
        return codesets;
    }

    public void setCodesets(Set<Codeset> codesets) {
        this.codesets = codesets;
    }

    public boolean isExpires() {
        return expires;
    }

    public void setExpires(boolean expires) {
        this.expires = expires;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(int validityDays) {
        this.validityDays = validityDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
