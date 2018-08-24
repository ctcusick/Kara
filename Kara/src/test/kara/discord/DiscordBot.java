package test.kara.discord;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;
import test.kara.bot.Kara;

/**
 * Represents a Discord client. A singleton.
 * 
 * 
 *
 */
public class DiscordBot {
	
	// FIELDS AND CONSTANTS
	
	// Singleton Implementation	
	private static DiscordBot SINGLETON;
	private static boolean INITIALIZED = false;

	// Fields
	private Kara owner;
	private IDiscordClient client;
	private EventDispatcher clientDispatch;

	// Constants
	public static final String BOT_PREFIX = "/";

	// CONSTRUCTORS

	/**
	 * @param authToken
	 * Discord bot authentication token obtainable through the Discord Developer Portal
	 */
	private DiscordBot(Kara owner, String authToken) {
		this.owner = owner;
		
		// Initialize IDiscordClient
		client = new ClientBuilder().withToken(authToken).build();
		clientDispatch = client.getDispatcher();

		// Register EventListeners with client
		clientDispatch.registerListener(new MessageReceivedEventListener(this));

		// Login to Discord
		this.login();
	}

	// FACTORY METHOD
	public static DiscordBot getInstance(Kara owner, String authToken) {
		if (!DiscordBot.INITIALIZED) {
			DiscordBot.SINGLETON = new DiscordBot(owner, authToken);
		}
		return SINGLETON;
	}
	
	// METHODS

	/**
	 * Attempt to send a message
	 * 
	 * @param channel
	 * @param message
	 */
	public void sendMessage(IChannel channel, String message) {
		if (!message.trim().isEmpty()) {
			RequestBuffer.request(new SendMessageRequest(channel, message));
		}
	}
	
	public void react(IMessage message, String react) {
		RequestBuffer.request(new ReactRequest(message, react));
	}
	
	public void reactSuccess(IMessage message) {
		RequestBuffer.request(new ReactRequest(message, Character.toString((char)9989)));
	}

	public void login() {
		if (client.isLoggedIn()) {
			client.logout();
		}
		client.login();	
	}
	
	public void logout() {
		client.logout();
		owner.die();
	}
	
	// Getters and Setters
	public Kara getOwner() {
		return this.owner;
	}

	// NESTED CLASSES

	// Discord API Requests
	public class SendMessageRequest implements RequestBuffer.IVoidRequest {
		private IChannel channel;
		private String message;

		private SendMessageRequest() {
		}

		public SendMessageRequest(IChannel channel, String message) {
			this.channel = channel;
			this.message = message;
		}

		@Override
		public void doRequest() {
			channel.sendMessage(message);
			System.out.println(message);
		}
	}
	
	public class ReactRequest implements RequestBuffer.IVoidRequest {
		private IMessage message;
		private String react;
		

		private ReactRequest() {
		}

		public ReactRequest(IMessage message, String react) {
			this.message = message;
			this.react = react;
		}

		@Override
		public void doRequest() {
			message.addReaction(ReactionEmoji.of(react));
			System.out.println("Reacted " + react + " to " + message.getContent() + " (Author StringID: " + message.getAuthor().getStringID() + ")");
		}
	}
}
