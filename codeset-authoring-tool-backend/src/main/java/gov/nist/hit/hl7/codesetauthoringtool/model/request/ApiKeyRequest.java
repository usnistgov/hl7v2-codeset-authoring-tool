package gov.nist.hit.hl7.codesetauthoringtool.model.request;


import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApiKeyRequest {
    @NotNull(message = "value is required")
    private String value;
    private String label;
    @NotNull(message = "expirationDate is required")
    private Date expirationDate;


    // Constructors, Getters, and Setters

    public ApiKeyRequest() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }


}
