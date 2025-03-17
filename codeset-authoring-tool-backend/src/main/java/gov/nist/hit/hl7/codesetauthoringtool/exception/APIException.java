package gov.nist.hit.hl7.codesetauthoringtool.exception;

import org.springframework.http.HttpStatus;

public class APIException extends Exception {
    private static final long serialVersionUID = 1L;
    private HttpStatus errorCode;
    public APIException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = HttpStatus.valueOf(errorCode);
    }
}
