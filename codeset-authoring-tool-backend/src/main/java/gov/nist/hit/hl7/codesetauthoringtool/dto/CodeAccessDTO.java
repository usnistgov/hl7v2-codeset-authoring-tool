package gov.nist.hit.hl7.codesetauthoringtool.dto;

public class CodeAccessDTO {
    private String value;
    private String codeSystem;
    private String displayText;
    private String regularExpression;
    private String usage;
    private Boolean pattern;

    public CodeAccessDTO() {
    }

    public CodeAccessDTO(String value, String codeSystem, String displayText, String regularExpression, String usage, Boolean pattern) {
        this.value = value;
        this.codeSystem = codeSystem;
        this.displayText = displayText;
        this.regularExpression = regularExpression;
        this.usage = usage;
        this.pattern = pattern;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Boolean isPattern() {
        return pattern;
    }

    public void setPattern(Boolean pattern) {
        this.pattern = pattern;
    }
}
