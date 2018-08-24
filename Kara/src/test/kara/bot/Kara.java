package test.kara.bot;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.telnet.InvalidTelnetOptionException;

import sx.blah.discord.handle.obj.IChannel;
import test.kara.discord.DiscordBot;
import test.kara.telnet.Terminal;

/**
 * Main class
 * 
 *
 */
public class Kara {
	private Terminal terminal = null;
	private IChannel terminalDiscordChannel = null;
	private DiscordBot discordBot = null;
	private String authKey;

	private Kara() {
	}

	public void die() {
		System.exit(69);
	}

	/**
	 * Starts a new Telnet Terminal
	 * 
	 * @return true if successful Will not be successful if a Terminal has already
	 *         been started
	 */
	public boolean startTerminal(IChannel discordChannel) {
		boolean successful = false;
		if (terminal == null) {
			try {
				this.terminal = new Terminal(this);
				this.terminalDiscordChannel = discordChannel;
				successful = true;
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTelnetOptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return successful;
	}

	public void killTerminal() {
		terminal.die();
		terminal = null;
	}
	
	public boolean terminalSendMessage(String message) {
		boolean successful = false;
		if (terminal != null) {
			return terminal.sendMessage(message);
		}
		return successful;
	}

	/**
	 * Send a control character in caret notation (e.g. ^A) to the Telnet Terminal.
	 * 
	 * @param ctrlChar Character from 64 to 95
	 * @return
	 */
	public boolean terminalSendControlChar(char ctrlChar) {
		boolean successful = false;
		if (terminal != null && ctrlChar >= 64 && ctrlChar <= 95) {
			successful = terminal.sendControlChar(ctrlChar);
		}
		else {
		}
		return successful;
	}

	public boolean terminalSendDel() {
		boolean successful = false;
		if (terminal != null) {
			return terminal.sendDel();
		}
		return successful;
	}

	public boolean discordSendMessage(String message) {
		boolean successful = false;
		if (terminal != null && terminalDiscordChannel != null) {
			discordBot.sendMessage(terminalDiscordChannel, message);
			return true;
		}
		return successful;
	}

	public static void main(String[] args) {
		Kara kara = new Kara();
		ConfigParser configParser;
		try {
			configParser = new ConfigParser("kara_config.xml");
		} catch (ConfigParserException e) {
			e.printStackTrace();
			configParser = null;
			System.exit(2);
		}
		kara.discordBot = DiscordBot.getInstance(kara, configParser.getConfigValue("authKey"));
	}

}