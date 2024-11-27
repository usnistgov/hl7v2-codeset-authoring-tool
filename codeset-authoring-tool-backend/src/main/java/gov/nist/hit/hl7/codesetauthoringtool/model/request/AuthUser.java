package gov.nist.hit.hl7.codesetauthoringtool.model.request;

import java.util.List;

public class AuthUser {
    private String username;
    private Boolean administrator;
    private List<String> roles;

    public AuthUser() {
    }

    public AuthUser(String username, Boolean administrator, List<String> roles) {
        this.username = username;
        this.administrator = administrator;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
