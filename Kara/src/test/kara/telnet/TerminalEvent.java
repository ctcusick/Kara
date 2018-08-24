package test.kara.telnet;

import java.util.EventObject;

public abstract class TerminalEvent extends EventObject {
	private static final long serialVersionUID = 567123784632727233L;

	public TerminalEvent(Object source) {
		super(source);
	}

}
