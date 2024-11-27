package gov.nist.hit.hl7.codesetauthoringtool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "apikeys")
public class ApiKey {

    @Id
    private String id;

    @Column(unique = true)
    private String token;
    private String name;
    private Date expireAt;
    private Date createdAt;

    private String username;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "codeset_apikey",
            joinColumns = @JoinColumn(name = "apikey_id"),
            inverseJoinColumns = @JoinColumn(name = "codeset_id")
    )
    private Set<Codeset> codesets = new HashSet<>();
    @JsonIgnore
    public boolean isValid() {
        return !isExpired();
    }
    @JsonIgnore
    public boolean isExpired() {
        if(expireAt != null) {
            Date now = new Date();
            return expireAt.before(now) || expireAt.equals(now);
        } else {
            return false;
        }
    }


    public ApiKey() {
    }

    @PrePersist
    public void generateId() {
        // This will generate a UUID and remove hyphens to get a 32-character string
        this.id = UUID.randomUUID().toString().replace("-", "");
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

    public Set<Codeset> getCodesets() {
        return codesets;
    }

    public void setCodesets(Set<Codeset> codesets) {
        this.codesets = codesets;
    }
}
