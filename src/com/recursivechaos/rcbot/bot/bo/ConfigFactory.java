package com.recursivechaos.rcbot.bot.bo;

/**
 * ConfigFactory loads the configuration settings from the config.xml file located on the build
 * path. It will read the XML, and create a configuration object that PircBotX can then used to
 * create and run mulitple bots at once. In getXMLConfig, listeners for the various plugins are
 * attached, and if a new pluigin is added, this must be added to know to look for, and attach
 * the new listener.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.admin.AdminListener;
import com.recursivechaos.rcbot.plugins.admin.Settings;
import com.recursivechaos.rcbot.plugins.calendar.CalendarListener;
import com.recursivechaos.rcbot.plugins.catfacts.CatFactListener;
import com.recursivechaos.rcbot.plugins.dadjokes.DadJokeListener;
import com.recursivechaos.rcbot.plugins.dice.DiceListener;
import com.recursivechaos.rcbot.plugins.newUserGreetingPlugin.NewUserGreetingListener;
import com.recursivechaos.rcbot.plugins.rcrover.RoverListener;
import com.recursivechaos.rcbot.plugins.redditPreview.RedditPreviewListener;
import com.recursivechaos.rcbot.plugins.stoopsnoop.AutobannerListener;
import com.recursivechaos.rcbot.plugins.stoopsnoop.LogListener;
import com.recursivechaos.rcbot.plugins.stoopsnoop.QueryListener;

public class ConfigFactory {
	// Start logger
	Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
	List<Settings> settingsList = new ArrayList<Settings>();

	/**
	 * getXMLConfig will then take the settings from the XML, and then 
	 * attach listeners to the new bot objects based on the XML settings.
	 * @param botSettings
	 * @return Configuration<? extends PircBotX> configuration file for PircBotX to create.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Configuration<? extends PircBotX> getXMLConfig(Settings botSettings) {
		// Create builder
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
		myBuilder.addListener(new AdminListener());
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
			myBuilder.addListener(new AutobannerListener());
		}
		if (mySettings.getRcrover() == true){
			myBuilder.addListener(new RoverListener());
		}
		// Build configuration and return
		Configuration<PircBotX> myConfiguration = myBuilder
				.buildConfiguration();
		return myConfiguration;
	}

	/**
	 * Loads settings from XML, and then creates as many bots as
	 * needed, as determined by the xml
	 * @return List<MyPircBotX> list of bot objects
	 */
	public List<MyPircBotX> loadBotsFromXML() {
		List<MyPircBotX> myBots = new ArrayList<MyPircBotX>();
		// Load initial config from XML
		readXML("config.xml");
		for (int i = 0; i < getTotalBots(); i++) {
			// Create settings object
			// Load settings from XML
			Settings botSettings = getBotSettings(i);
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

	/**
	 * Reads XML file provided, and adds to bot settings
	 * @param fileURL
	 */
	private void readXML(String fileURL) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(fileURL);
		try {
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<?> list = rootNode.getChildren("bot");
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				Settings botSettings = new Settings();
				botSettings.setNick(node.getChildText("nick"));
				botSettings.setPassword(node.getChildText("password"));
				botSettings.setServer(node.getChildText("server"));
				botSettings.setChannel(node.getChildText("channel"));
				botSettings.setRedditPreview(Boolean.valueOf(node
						.getChildText("redditpreview")));
				botSettings.setNewUserGreeting(Boolean.valueOf(node
						.getChildText("newusergreeting")));
				botSettings.setCalendar(Boolean.valueOf(node
						.getChildText("calendar")));
				botSettings.setCalendarUrl((node.getChildText("calendarurl")));
				botSettings
						.setDice(Boolean.valueOf((node.getChildText("dice"))));
				botSettings.setCatfacts(Boolean.valueOf((node
						.getChildText("catfacts"))));
				botSettings.setDadjokes(Boolean.valueOf((node
						.getChildText("dadjokes"))));
				botSettings.setDadjokeChance(Integer.parseInt((node
						.getChildText("dadjokechance"))));
				botSettings.setLogger(Boolean.valueOf((node
						.getChildText("logger"))));
				botSettings.setAdmin(node.getChildText("admin"));
				botSettings.setRcrover(Boolean.valueOf((node
						.getChildText("rcrover"))));
				settingsList.add(botSettings);
			}
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}
	
	/**
	 * Returns a particular botSettings object
	 * @param i	index
	 * @return	bot settings for that index
	 */
	private Settings getBotSettings(int i) {
		return settingsList.get(i);
	}

	/**
	 * Returns amount of bots called for
	 * @return total bots
	 */
	private int getTotalBots() {
		return settingsList.size();
	}
	
}
