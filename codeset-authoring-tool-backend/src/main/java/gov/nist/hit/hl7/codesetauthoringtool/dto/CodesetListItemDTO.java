package gov.nist.hit.hl7.codesetauthoringtool.dto;

public class CodesetListItemDTO {
    private String id;
    private String name;

    public CodesetListItemDTO(String id, String name) {
        this.id = id;
        this.name = name;
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
}
