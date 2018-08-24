package test.kara.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TerminalEventDispatcher {
	private Terminal owner;
	private InputStream commonsClientInputStream;
	private ConcurrentLinkedQueue<TerminalEventListener> eventListeners = new ConcurrentLinkedQueue<TerminalEventListener>();
	private CommonsClientInputStreamAdapter commonsClientInputStreamAdapter;
	private Thread commonsClientInputStreamAdapterThread;
	
	public TerminalEventDispatcher(Terminal owner, InputStream commonsClientInputStream) {
		this.owner = owner;
		this.commonsClientInputStream = commonsClientInputStream;
		commonsClientInputStreamAdapter = new CommonsClientInputStreamAdapter(this,commonsClientInputStream);
		commonsClientInputStreamAdapterThread = new Thread(commonsClientInputStreamAdapter);
		commonsClientInputStreamAdapterThread.start();
	}

	public void registerEventListener(TerminalEventListener listener) {
		eventListeners.add(listener);
	}
	
	public void unregisterEventListener(TerminalEventListener listener) {
		eventListeners.remove(listener);
	}
	
	public <T extends TerminalEvent> void dispatch(T event) throws TerminalEventTypeMismatchException {
		for (TerminalEventListener listener : eventListeners) {
			if (TerminalEventDispatcher.getEventEventListenerType(event).isInstance(listener)) {
				listener.handle(event);
			}
		}
	}
	
	public static <T extends TerminalEvent> Class getEventEventListenerType(T event) {
		if (MessageReceivedEvent.class.isInstance(event)) {
			return MessageReceivedEventListener.class;
		}
		else {
			return TerminalEventListener.class;
		}
	}
	
	/**
	 * Render this object useless
	 * Will probably screw up the IO
	 */
	public void die() {
		commonsClientInputStreamAdapter.die();
		commonsClientInputStreamAdapterThread.interrupt();
		for (TerminalEventListener listener : eventListeners) {
			unregisterEventListener(listener);
		}
	}
	
	// Getters and Setters
	
	public Terminal getOwner() {
		return this.owner;
	}
	
	// NESTED CLASSES
	
	private class CommonsClientInputStreamAdapter implements Runnable {
		private TerminalEventDispatcher owner;
		private InputStream commonsClientInputStream;
		private BufferedReader bufferedReader;
		private boolean live = true;
		
		public CommonsClientInputStreamAdapter(TerminalEventDispatcher owner, InputStream input) {
			this.owner = owner;
			this.commonsClientInputStream = input;
			this.bufferedReader = new BufferedReader(new InputStreamReader(commonsClientInputStream));
		}
		
		public void die() {
			this.live = false;
		}
		
		@Override
		public void run() {
			while (live) {
				MessageReceivedEvent messageEvent;
				try {
					messageEvent = new MessageReceivedEvent (this.owner,bufferedReader.readLine());
					owner.dispatch(messageEvent);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TerminalEventTypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		
	}
}
