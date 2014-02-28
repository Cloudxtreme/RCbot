package com.recursivechaos.rcbot.plugins.stoopsnoop.log;

/**
 * EventLogDAO defines the functions required for eventlog. Impl files should be
 * placed in rcbot.plugins.persistance.*.dao
 * 
 * @author Andrew Bell www.recursivechaos.com
 * 
 */
import org.pircbotx.hooks.Event;

import com.recursivechaos.rcbot.bot.object.BotException;
import com.recursivechaos.rcbot.bot.object.MyPircBotX;

public interface EventLogDAO {

	public void logEvent(Event<MyPircBotX> event) throws BotException;
}
