package com.recursivechaos.rcbot.bot;
/**
 * ConfigFactory loads the configuration settings from file, and then 
 * creates a Configuration object for the pircbotx object.
 * 
 * @author Andrew Bell
 */
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.plugins.calendar.CalendarListener;
import com.recursivechaos.rcbot.plugins.catfacts.CatFactListener;
import com.recursivechaos.rcbot.plugins.dadjokes.DadJokeListener;
import com.recursivechaos.rcbot.plugins.dice.DiceListener;
import com.recursivechaos.rcbot.plugins.newUserGreetingPlugin.NewUserGreetingListener;
import com.recursivechaos.rcbot.plugins.redditPreview.RedditPreviewListener;
import com.recursivechaos.rcbot.settings.SettingsBO;

public class ConfigFactory {
	// Start logger
	Logger logger = LoggerFactory.getLogger(ConfigFactory.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Configuration getConfig() {
		// Create bulider
		Builder myBuilder = new Configuration.Builder();
		SettingsBO mySettings = new SettingsBO();
		// Load settings
		mySettings.loadSettingsFromFile();
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
			myBuilder.addListener(new CalendarListener(mySettings));
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
		// Build configuration and return
		Configuration<PircBotX> myConfiguration = myBuilder
				.buildConfiguration();
		return myConfiguration;
	}
}
