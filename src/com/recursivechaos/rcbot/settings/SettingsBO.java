package com.recursivechaos.rcbot.settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SettingsBO handles global settings for the bot.
 * 
 * @author Andrew Bell
 * 
 */
public class SettingsBO extends Settings {
	// Start logger
	Logger logger = LoggerFactory.getLogger(SettingsBO.class);

	/**
	 * Loads settings from BotConfig.txt
	 */
	public void loadSettingsFromFile() {
		List<String> settingsList = new ArrayList<String>();
		try {
			settingsList = readFile();
		} catch (Exception e) {
			logger.error("Unable to read from file. " + e.getMessage());
		}
		parseSettings(settingsList);
	}

	/**
	 * Takes list of settings, and inputs them to the settings object
	 * 
	 * @param settingsList
	 */
	private void parseSettings(List<String> settingsList) {
		for (int i = 0; i < settingsList.size(); i++) {
			String line = settingsList.get(i);
			switch (line) {
			case ("#NICK"):
				this.setNick(settingsList.get(i + 1));
				break;
			case ("#PASSWORD"):
				this.setPassword(settingsList.get(i + 1));
				break;
			case ("#SERVER"):
				this.setServer(settingsList.get(i + 1));
				break;
			case ("#CHANNEL"):
				this.setChannel("#" + settingsList.get(i + 1));
				break;
			case ("#REDDITPREVIEW"):
				this.setRedditPreview(Boolean.valueOf(settingsList.get(i + 1)));
				break;
			case ("#NEWUSERGREETING"):
				this.setNewUserGreeting(Boolean.valueOf(settingsList.get(i + 1)));
			}
		}
	}

	/**
	 * Reads file BotConfig.txt and returns a List<String> of settings
	 * 
	 * @return List<String> of settings
	 * @throws Exception
	 */
	private List<String> readFile() throws Exception {
		List<String> settingsList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("BotConfig.txt"));
		try {
			String line = br.readLine();
			while (line != null) {
				settingsList.add(line);
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return settingsList;
	}
}
