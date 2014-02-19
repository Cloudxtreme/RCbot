package com.recursivechaos.rcbot.bot;

/**
 * ConfigFactory loads the configuration settings from file, and then 
 * creates a Configuration object for the pircbotx object.
 * 
 * @author Andrew Bell
 */
import java.util.ArrayList;
import java.util.List;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.bot.object.MainConfigBO;
import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.calendar.CalendarListener;
import com.recursivechaos.rcbot.plugins.catfacts.CatFactListener;
import com.recursivechaos.rcbot.plugins.dadjokes.DadJokeListener;
import com.recursivechaos.rcbot.plugins.dice.DiceListener;
import com.recursivechaos.rcbot.plugins.eventlog.LogListener;
import com.recursivechaos.rcbot.plugins.eventlog.QueryListener;
import com.recursivechaos.rcbot.plugins.newUserGreetingPlugin.NewUserGreetingListener;
import com.recursivechaos.rcbot.plugins.redditPreview.RedditPreviewListener;
import com.recursivechaos.rcbot.settings.Settings;

public class ConfigFactory {
	// Start logger
	Logger logger = LoggerFactory.getLogger(ConfigFactory.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Configuration<? extends PircBotX> getXMLConfig(Settings botSettings) {
		// Create bulider
		Builder myBuilder = new Configuration.Builder();
		Settings mySettings = botSettings;
		// Add generics to builder
		myBuilder
				.setName(mySettings.getNick())
				.setLogin("RCbot")
				.setAutoNickChange(true)
				.setCapEnabled(true)
				.addCapHandler(
						new TLSCapHandler(new UtilSSLSocketFactory()
								.trustAllCertificates(), true))
				.setAutoReconnect(true)
				.setServerHostname(mySettings.getServer())
				.addAutoJoinChannel(mySettings.getChannel())
				.setNickservPassword(mySettings.getPassword());
		// Add necessary listeners
		if (mySettings.getRedditPreview() == true) {
			myBuilder.addListener(new RedditPreviewListener());
		}
		if (mySettings.getNewUserGreeting() == true) {
			myBuilder.addListener(new NewUserGreetingListener());
		}
		if (mySettings.getCalendar() == true) {
			myBuilder.addListener(new CalendarListener());
		}
		if (mySettings.getDice() == true) {
			myBuilder.addListener(new DiceListener());
		}
		if (mySettings.getCatfacts() == true) {
			myBuilder.addListener(new CatFactListener());
		}
		if (mySettings.getDadjokes() == true) {
			myBuilder.addListener(new DadJokeListener());
		}
		if (mySettings.getLogger() == true) {
			myBuilder.addListener(new LogListener());
			myBuilder.addListener(new QueryListener());
		}
		// Build configuration and return
		Configuration<PircBotX> myConfiguration = myBuilder
				.buildConfiguration();
		return myConfiguration;
	}

	public List<MyPircBotX> loadBotsFromXML() {
		List<MyPircBotX> myBots = new ArrayList<MyPircBotX>();
		// Load initial config from XML
		MainConfigBO config = new MainConfigBO("config.xml");
		for (int i = 0; i < config.getTotalBots(); i++) {
			// Create settings object
			// Load settings from XML
			Settings botSettings = config.getBotSettings(i);
			// Create myBot object
			// Attach settings to bot
			MyPircBotX myBot = new MyPircBotX(getXMLConfig(botSettings),
					botSettings);
			// Add to arrayList
			myBots.add(myBot);
		}
		// Return list

		return myBots;
	}
}
