package com.recursivechaos.rcbot.bot;

/**
 * MainBotConsole creates a configuration factory, gets the Configuration
 * object, and creates a bot object with it.
 * 
 * @author Andrew Bell
 *
 */
import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainBotConsole {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Start logger
		Logger logger = LoggerFactory.getLogger(MainBotConsole.class);
		// Create config factory
		ConfigFactory myConfigFactory = new ConfigFactory();
		logger.info("Created myConfigFactory");
		// Build configuration from factory
		Configuration<PircBotX> configuration = myConfigFactory.getConfig();
		logger.info("configuration loaded from myConfigFactory");
		// Create bot and connect
		PircBotX bot = new PircBotX(configuration);
		try {
			logger.info("Starting bot.");
			bot.startBot();
			logger.info("Bot Stopped.");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException: " + e.getMessage());
		} catch (IrcException e) {
			e.printStackTrace();
			logger.error("IrcException: " + e.getMessage());
		}
	}
}
