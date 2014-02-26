package com.recursivechaos.rcbot.bot.object;

/**
 * MyPircBotX is a custom PircBotX with a settings object attached to it.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import com.recursivechaos.rcbot.plugins.admin.Settings;

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
	
	public MyPircBotX getBot(){
		return this;
	}

}
