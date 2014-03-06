package com.recursivechaos.rcbot.bot;

/**
 * MainBotConsole creates a configuration factory, gets the Configuration
 * object, and creates a bot object with it.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pircbotx.MultiBotManager;

import com.recursivechaos.rcbot.bot.bo.ConfigFactory;
import com.recursivechaos.rcbot.bot.object.MyPircBotX;

public class MainBotConsole {

	public static void main(String[] args) {
		// Create configuration factory
		ConfigFactory myConfigFactory = new ConfigFactory();
		// Load bot array from file
		List<MyPircBotX> botList = new ArrayList<>();
		botList = myConfigFactory.loadBotsFromXML();
		// Create multibot manager
		MultiBotManager<MyPircBotX> botManager = new MultiBotManager<MyPircBotX>();
		Iterator<MyPircBotX> itr = botList.iterator();
		while (itr.hasNext()) {
			MyPircBotX thisBot = itr.next();
			botManager.addBot(thisBot);
		}
		// Start botManager
		botManager.start();
	}
}
