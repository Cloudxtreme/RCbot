package com.recursivechaos.rcbot.plugins.eventlog;
/**
 * LogListener listens for events and passes them to the EventLogDAO
 * 
 * @author Andrew Bell
 * 
 */
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.plugins.persistance.mySQL.EventLogDAOImpl;

public class LogListener extends ListenerAdapter<PircBotX> {
	Logger logger = LoggerFactory.getLogger(LogListener.class);
	EventLogDAO eventlog = new EventLogDAOImpl();
	
	@Override
	public void onEvent(final Event<PircBotX> event) {
		eventlog.logEvent(event);
		try {
			super.onEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}