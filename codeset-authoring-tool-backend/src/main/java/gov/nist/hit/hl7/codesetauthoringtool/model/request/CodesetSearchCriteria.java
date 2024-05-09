package gov.nist.hit.hl7.codesetauthoringtool.model.request;

public class CodesetSearchCriteria {
    private String scope;
    private String version;

    private  String name;

    public CodesetSearchCriteria() {

    }
    public CodesetSearchCriteria(String scope, String version, String name) {
        this.scope = scope;
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
