package test.kara.telnet;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.apache.commons.net.telnet.TelnetClient;

import test.kara.bot.Kara;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;

public class Terminal {
	private Kara owner;
	private TelnetClient commonsClient;
	private SuppressGAOptionHandler sgaHandler;
	private EchoOptionHandler echoHandler;
	private TerminalEventDispatcher dispatch;
	
	private static final boolean[] DEFAULT_OPTION_PARAMS = { false, true, false, true };

	private Terminal() throws SocketException, IOException, InvalidTelnetOptionException {
	}
	
	public Terminal(Kara owner) throws SocketException, IOException, InvalidTelnetOptionException {
		this("rainmaker.wunderground.com", 3000, DEFAULT_OPTION_PARAMS, DEFAULT_OPTION_PARAMS);
		this.owner = owner;
	}

	public Terminal(String host, int port, boolean[] sgaParams, boolean[] echoParams)
			throws IOException, SocketException, InvalidTelnetOptionException {
		// Spawn client, option handlers
		commonsClient = new TelnetClient();
		// Spawn option listeners
		sgaHandler = new SuppressGAOptionHandler(sgaParams[0], sgaParams[1], sgaParams[2], sgaParams[3]);
		echoHandler = new EchoOptionHandler(echoParams[0], echoParams[1], echoParams[2], echoParams[3]);

		// Handle telnet options
		commonsClient.addOptionHandler(sgaHandler);
		commonsClient.addOptionHandler(echoHandler);

		// Connect to server
		commonsClient.connect(host, port);

		// Attach event dispatcher
		dispatch = new TerminalEventDispatcher(this, this.commonsClient.getInputStream());

		// Attach event listeners to event dispatcher
		dispatch.registerEventListener(new MessageReceivedEventListener());
	}

	public TerminalEventDispatcher getTerminalEventDispatcher() {
		return dispatch;
	}

	/**
	 * Send a message to the server.
	 * 
	 * @param message
	 * @return true if successful
	 */
	public boolean sendMessage(String message) {
		try {
			// Send message
			System.err.println(message);
			commonsClient.getOutputStream().write(message.getBytes(Charset.forName("US-ASCII")),0,message.getBytes(Charset.forName("US-ASCII")).length);
			// Send carriage return
			commonsClient.getOutputStream().write(13);
			// Flush
			commonsClient.getOutputStream().flush();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Sends a control character to the server
	 * @param control
	 * Control character in caret notation (e.g. ^C)
	 * @return
	 * true if successful
	 */
	public boolean sendControlChar(char control) {
		try {
			commonsClient.getOutputStream().write((char) (control - 64));
			commonsClient.getOutputStream().flush();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
	
	/**
	 * Sends the DEL character to the server
	 * @return
	 * true if successful
	 */
	public boolean sendDel() {
		try {
			// Send message
			commonsClient.getOutputStream().write(127);
			commonsClient.getOutputStream().flush();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Disconnect the terminal and render it useless
	 */
	public void die() {
		try {
			commonsClient.deleteOptionHandler(1);
			commonsClient.deleteOptionHandler(3);
			sgaHandler = null;
			echoHandler = null;
			commonsClient.disconnect();
			dispatch.die();
			dispatch = null;
		} catch (InvalidTelnetOptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Getters and Setters
	public Kara getOwner() {
		return this.owner;
	}
	
}
