package gov.nist.hit.hl7.codesetauthoringtool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;
    @Id
    private String id;
    private String token;
    private String userId;
    private String email;
    private Date expiryDate;

    public PasswordResetToken(String token, String userId, String email, Date expiryDate) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.expiryDate = expiryDate;
    }

    public PasswordResetToken() {
    }

    @PrePersist
    public void generateId() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public static int getExpiration() {
        return EXPIRATION;
    }

}
