package gov.nist.hit.hl7.codesetauthoringtool.dto;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.*;

public class ApiKeyDTO {
    private String id;

    private String token;
    private String name;
    private Date expireAt;
    private Date createdAt;

    private String username;
    private List<CodesetListItemDTO> codesets = new ArrayList<>();

    public ApiKeyDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CodesetListItemDTO> getCodesets() {
        return codesets;
    }

    public void setCodesets(List<CodesetListItemDTO> codesets) {
        this.codesets = codesets;
    }
}
