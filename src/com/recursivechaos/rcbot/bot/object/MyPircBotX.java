package com.recursivechaos.rcbot.bot.object;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import com.recursivechaos.rcbot.settings.Settings;

public class MyPircBotX extends PircBotX {
	Settings mySettings;

	public MyPircBotX(Configuration<? extends PircBotX> configuration,
			Settings mySettings) {
		super(configuration);
		this.mySettings = mySettings;
	}

	public Settings getSettings() {
		return mySettings;
	}

}
