package gov.nist.hit.hl7.codesetauthoringtool.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class ApiKeyDisplay {

    private String id;
    private String name;
    private Date createdAt;
    private Date expireAt;
    private Set<Codeset> codesets;

    public ApiKeyDisplay() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public Set<Codeset> getCodesets() {
        return codesets;
    }

    public void setCodesets(Set<Codeset> codesets) {
        this.codesets = codesets;
    }
}
