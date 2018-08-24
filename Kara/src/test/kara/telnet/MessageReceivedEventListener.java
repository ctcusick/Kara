package test.kara.telnet;

public class MessageReceivedEventListener implements TerminalEventListener {

	@Override
	public <T extends TerminalEvent> void handle(T event) throws TerminalEventTypeMismatchException {
		if (TerminalEventDispatcher.getEventEventListenerType(event).equals(this.getClass())) {
			String messageReceived = ((MessageReceivedEvent) event).getMessage();
			TerminalEventDispatcher owner = (TerminalEventDispatcher) event.getSource();
			owner.getOwner().getOwner().discordSendMessage(messageReceived);
		}
		else {
			throw new TerminalEventTypeMismatchException();
		}
	}
	
	
}
