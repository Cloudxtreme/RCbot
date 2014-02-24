package com.recursivechaos.rcbot.plugins.eventlog;

/**
 * EventLogDAO defines the functions required for eventlog. Impl files should be
 * placed in rcbot.plugins.persistance.*
 * 
 * @author Andrew Bell
 * 
 */
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;

public interface EventLogDAO {

	public void logEvent(Event<PircBotX> event);
}
