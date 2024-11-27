package gov.nist.hit.hl7.codesetauthoringtool.model;

public class GeneratedApiKey extends ApiKey{
    private String plainToken;

    public GeneratedApiKey() {
    }
    public GeneratedApiKey(ApiKey apiKey, String plainToken) {
        this.plainToken = plainToken;
        this.setId(apiKey.getId());
        this.setName(apiKey.getName());
        this.setCreatedAt(apiKey.getCreatedAt());
        this.setCodesets(apiKey.getCodesets());
        this.setExpireAt(apiKey.getExpireAt());
    }

    public String getPlainToken() {
        return plainToken;
    }

    public void setPlainToken(String plainToken) {
        this.plainToken = plainToken;
    }
}
