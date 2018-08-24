package test.kara.telnet;

public interface TerminalEventListener {
	public <T extends TerminalEvent> void handle(T event) throws TerminalEventTypeMismatchException;
}
