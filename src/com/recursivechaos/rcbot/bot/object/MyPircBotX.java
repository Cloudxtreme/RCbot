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

	/**
	 * Creates a MyPircBotX object given a configuration, and Settings
	 * @param configuration	PircBotX's configuration object
	 * @param mySettings	MyPircBotX's setting object (to hold additional settings)
	 */
	public MyPircBotX(Configuration<? extends PircBotX> configuration,
			Settings mySettings) {
		super(configuration);
		this.mySettings = mySettings;
	}

	/**
	 * Get settings
	 * @return settings object
	 */
	public Settings getSettings() {
		return mySettings;
	}
	
	/**
	 * Get bot
	 * @return bot object
	 */
	public MyPircBotX getBot(){
		return this;
	}

}
