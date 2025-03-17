package gov.nist.hit.hl7.codesetauthoringtool.model.response;

public class APIResponseMessage {
    String message;

    public APIResponseMessage() {
    }

    public APIResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
