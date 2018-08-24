package test.kara.telnet;

public class MessageReceivedEvent extends TerminalEvent {
	private static final long serialVersionUID = 5620739578276145761L;
	private String message;
	
	public MessageReceivedEvent(Object source) {
		super(source);
	}
	
	public MessageReceivedEvent(Object source, String message) {
		this(source);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
