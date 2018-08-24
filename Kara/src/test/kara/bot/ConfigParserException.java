package test.kara.bot;

public class ConfigParserException extends Exception {
	String message = "";
	
	public ConfigParserException(String msg) {
		this.message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
