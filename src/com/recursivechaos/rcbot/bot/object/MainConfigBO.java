package com.recursivechaos.rcbot.bot.object;

/**
 * MainConfigBO handles reading the configuration file, and creating a Settings list
 * 
 * @author Andrew Bell
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.recursivechaos.rcbot.settings.Settings;

public class MainConfigBO {
	List<Settings> settingsList = new ArrayList<Settings>();

	public MainConfigBO(String fileURL) {
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
				settingsList.add(botSettings);
			}
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	public Settings getBotSettings(int i) {
		return settingsList.get(i);
	}

	public int getTotalBots() {
		return settingsList.size();
	}
}