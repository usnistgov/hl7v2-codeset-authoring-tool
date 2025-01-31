package gov.nist.hit.hl7.codesetauthoringtool.dto;
import java.util.Date;
import java.util.List;

public class CodesetVersionMetadataAccessDTO {
    private String id;
    private String name;
    private Date date;
    private int numberOfCodes;
    private String version;


    public CodesetVersionMetadataAccessDTO() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumberOfCodes() {
        return numberOfCodes;
    }

    public void setNumberOfCodes(int numberOfCodes) {
        this.numberOfCodes = numberOfCodes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
