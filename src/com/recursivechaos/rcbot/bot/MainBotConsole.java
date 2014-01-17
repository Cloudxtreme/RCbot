package com.recursivechaos.rcbot.bot;

/**
 * MainBotConsole is responsible for the initial configuration, and connection 
 * of the irc bot. The following flags can be passed on startup, otherwise the 
 * user will be prompted to input settings (in any order)
 * 
 * -n nickname 		represents the bot nickname
 * -p password 		nickserv password
 * -s server 		(i.e. chat.freenode.org)
 * -c channel		initial channel to start bot on
 * -a admin			nick of the "admin" user to control bot (to be implemented)
 * 
 * @author Andrew Bell
 *
 */
import java.io.IOException;
import java.util.Scanner;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.plugins.social.SocialPluginListener;
import com.recursivechaos.rcbot.plugins.urlAnnounce.UrlAnnouncePluginListener;
import com.recursivechaos.rcbot.settings.SettingsBO;

public class MainBotConsole {
	static SettingsBO settings = new SettingsBO();

	/**
	 * Fetches the configuration settings, if not found in the arguments, will
	 * prompt user for settings and store them.
	 * 
	 * @param args
	 *            command line argument. See header comments for full desc.
	 */
	private static void getConfigSettings(String[] args) {
		Scanner input = new Scanner(System.in);
		for (int x = 0; x < args.length; x++) {
			if (args[x].contains("-n")) {
				settings.setNick(args[x + 1]);
			} else if (args[x].contains("-p")) {
				settings.setPassword(args[x + 1]);
			} else if (args[x].contains("-s")) {
				settings.setServer(args[x + 1]);
			} else if (args[x].contains("-c")) {
				settings.setChannel(args[x + 1]);
			} else if (args[x].contains("-a")) {
				settings.setAdmin(args[x + 1]);
			}
		}
		if (settings.getNick().isEmpty()) {
			System.out.print("Enter nick: ");
			settings.setNick(input.nextLine());
		}
		if (settings.getPassword().isEmpty()) {
			System.out.print("Enter password: ");
			settings.setPassword(input.nextLine());
		}
		if (settings.getServer().isEmpty()) {
			System.out.print("Enter server: ");
			settings.setServer(input.nextLine());
		}
		if (settings.getChannel().isEmpty()) {
			System.out.print("Enter channel: ");
			settings.setChannel(input.nextLine());
		}
		if (settings.getAdmin().isEmpty()) {
			System.out.print("Enter admin: ");
			settings.setAdmin(input.nextLine());
		}
		input.close();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		// Start logger
		Logger logger = LoggerFactory.getLogger(MainBotConsole.class);
		logger.info("Loading bot configuration.");
		// Get inputs
		getConfigSettings(args);
		// Load configuration
		Configuration configuration = new Configuration.Builder()
				.setName(settings.getNick())
				// Set the nick of the bot.
				.setLogin("RCbot")
				// login part of hostmask, eg name:login@host
				.setAutoNickChange(true)
				// Automatically change nick
				.setCapEnabled(true)
				// Enable CAP features
				.addCapHandler(
						new TLSCapHandler(new UtilSSLSocketFactory()
								.trustAllCertificates(), true))
				.addListener(new SocialPluginListener())
				.addListener(new UrlAnnouncePluginListener())
				.setServerHostname(settings.getServer())
				.addAutoJoinChannel(settings.getChannel())
				.setNickservPassword(settings.getPassword())
				.buildConfiguration();
		logger.info("Configuration loaded.");
		// Start bot
		try {
			PircBotX bot = new PircBotX(configuration);
			bot.startBot();
			logger.info("Bot Started.");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException: " + e.getMessage());
		} catch (IrcException e) {
			e.printStackTrace();
			logger.error("IrcException: " + e.getMessage());
		} finally {
			logger.info("Bot stopped.");
		}
	}
}
