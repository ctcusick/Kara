package test.kara.discord;

import java.util.regex.Pattern;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;

public class MessageReceivedEventListener implements IListener<MessageReceivedEvent> {
	private DiscordBot owner;
	public static final String MESSAGE_SEND_FAIL_MESSAGE = "I couldn't send the message to the server.";

	public MessageReceivedEventListener(DiscordBot discordBot) {
		this.owner = discordBot;
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		IMessage message = event.getMessage();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();

		// DEBUG
		System.out.println(author.getName() + "#" + author.getDiscriminator() + " (" + author.getStringID() + "): "
				+ message.getContent());

		// Check if bot command
		boolean isCommand = false;

		if (message.getContent().split(" ")[0].startsWith(DiscordBot.BOT_PREFIX, 0)) {
			isCommand = true;
		}

		// Handle bot commands
		if (isCommand) {
			// Cases for commands

			// Includes command with BOT_PREFIX
			String[] commandArgs = message.getContent().split(" ");

			// Does not include BOT_PREFIX
			String commandName = commandArgs[0].split(DiscordBot.BOT_PREFIX).length < 2 ? ""
					: commandArgs[0].split(DiscordBot.BOT_PREFIX)[1];

			// Empty if no args
			String argsString = message.getFormattedContent().replaceFirst(message.getContent().split(" ")[0], "");
			argsString = argsString.startsWith(" ") ? argsString.replaceFirst(" ", "") : argsString;

			switch (commandName) {
			case "test":
				commandTest(channel, argsString);
				break;
			case "name":
				commandName(channel);
				break;
			case "help":
				commandHelp(channel);
				break;
			case "connect":
				commandConnect(channel);
				break;
			case "send":
				commandSend(channel, message, argsString);
				break;
			case "sendCtrl":
				commandSendCtrl(channel, message, argsString);
				break;
			case "sendDel":
				commandSendDel(channel, message);
				break;
			case "disconnect":
				commandDisconnect(channel);
				break;
			case "die":
				commandDie(channel);
				break;
			default:
				commandDefault(channel, message);
				break;
			}
		}
	}

	// Discord Commands
	
	/**
	 * Repeats arguments sent by user
	 * 
	 * @param channel
	 * @param message
	 */
	private void commandTest(IChannel channel, String argsString) {
		if (!argsString.isEmpty()) {
			owner.sendMessage(channel, argsString);
		}
	}

	private void commandName(IChannel channel) {
		owner.sendMessage(channel, "My name is Kara.");
	}

	private void commandHelp(IChannel channel) {
		owner.sendMessage(channel,
				"I am an AX400 android. I can look after your house, do the cooking, mind the kids... I organize your appointments. I speak 300 languages, and I am entirely at your disposal as a sexual partner. No need to feed me or recharge me. I am equipped with a quantic battery that makes me autonomous for 173 years.\nI'm here to help you to Telnet servers.");
		owner.sendMessage(channel, "/connect\nConnect to the default Telnet server\n\n/send [text]\nSend the specified text to the server\n\n/sendCtrl [caret notation character]\nSend the specified ctrl character to the server\n\n/sendDel\nSend the delete character to the server\n\n/disconnect\nDisconnect from the server\n\n/help\nDisplay this message.");
	}

	private void commandConnect(IChannel channel) {
		owner.getOwner().startTerminal(channel);
	}

	private void commandSend(IChannel channel, IMessage message, String argsString) {
		boolean success = owner.getOwner().terminalSendMessage(argsString);
		if (success) {
			owner.reactSuccess(message);
		} else {
			owner.sendMessage(channel, MESSAGE_SEND_FAIL_MESSAGE);
		}
	}

	private void commandSendCtrl(IChannel channel, IMessage message, String argsString) {
		char ctrlChar = 0;
		if (!argsString.isEmpty()) {
			ctrlChar = argsString.charAt(0);
			boolean success = owner.getOwner().terminalSendControlChar(ctrlChar);
			if (success) {
				owner.reactSuccess(message);
			} else {
				owner.sendMessage(channel, MESSAGE_SEND_FAIL_MESSAGE);
			}
		} else {
			owner.sendMessage(channel, MESSAGE_SEND_FAIL_MESSAGE);
		}
	}

	private void commandSendDel(IChannel channel, IMessage message) {
		boolean success = owner.getOwner().terminalSendDel();
		if (success) {
			owner.reactSuccess(message);
		} else {
			owner.sendMessage(channel, MESSAGE_SEND_FAIL_MESSAGE);
		}
	}

	private void commandDisconnect(IChannel channel) {
		owner.getOwner().killTerminal();
	}
	
	private void commandDie(IChannel channel) {
		owner.sendMessage(channel, "https://www.youtube.com/watch?v=G0KTUysrwgQ&start=260&end=281");
		owner.logout();
	}

	/**
	 * Default command. Repeats message sent by user
	 * 
	 * @param channel
	 * @param message
	 */
	private void commandDefault(IChannel channel, IMessage message) {
		owner.sendMessage(channel, message.getContent());
	}
}
