package gov.nist.hit.hl7.codesetauthoringtool.dto;

import java.util.Date;

public class VersionAccessDTO {
    private String version;
    private Date date;

    public VersionAccessDTO(String version, Date date) {
        this.version = version;
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
