package gov.nist.hit.hl7.codesetauthoringtool.model.request;

public class ResetPasswordRequest {
    private String email;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
