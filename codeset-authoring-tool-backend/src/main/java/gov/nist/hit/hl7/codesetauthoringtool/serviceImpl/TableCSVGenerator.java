package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gov.nist.hit.hl7.codesetauthoringtool.model.Code;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;


public class TableCSVGenerator {

	private static final char DEFAULT_SEPARATOR = ',';


	private String escapeNull(String s){
		if(s == null) return "";
		return s;
	}

	public String writeLine(String w, List<String> values) {
        return writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

	public String writeLine(String w, List<String> values, char separators) {
    	return writeLine(w, values, separators, ' ');
    }

	public String writeLine(String w, List<String> values, char separators, char customQuote) {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        return w + sb.toString();
    }
    
    private static String followCVSformat(String value) {

        String result = value;
        if(result == null) result = "";
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }
    
    public String generate(List<Code> codes) {
    	
    	String csvString = "";
		List<String> values = new ArrayList<String>();
		values = new ArrayList<String>();
		values.add("value");
		values.add("pattern");
		values.add("description");
		values.add("codeSystem");
		values.add("usage");
		values.add("comments");
		csvString = this.writeLine(csvString, values, ',', '"');
		
		for(Code c: codes){
			values = new ArrayList<String>();
			values.add(c.getCode());
			values.add(c.getPattern());
			values.add(c.getDescription());
			values.add(c.getSystem());
			if(c.getUsage() == null) c.setUsage("P");
			values.add(c.getUsage().toString());
			values.add(c.getComments());
			csvString = this.writeLine(csvString, values, ',', '"');	
		}
		return csvString;
	}
    	
    
    
}
