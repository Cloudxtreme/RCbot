package com.recursivechaos.rcbot.plugins.stoopsnoop;

/**
 * LogListener listens for events and passes them to the EventLogDAO
 * 
 * @author Andrew Bell www.recursivechaos.com
 * 
 */
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.EventLogDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.log.EventLogDAO;

public class LogListener extends ListenerAdapter<PircBotX> {
	Logger logger = LoggerFactory.getLogger(LogListener.class);
	EventLogDAO eventlog = new EventLogDAOImpl();

	@Override
	public void onEvent(final Event<PircBotX> event) throws Exception {
		eventlog.logEvent(event);
		super.onEvent(event);
	}
}