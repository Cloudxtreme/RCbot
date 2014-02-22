package com.recursivechaos.rcbot.plugins.eventlog;

/**
 * QueryListener handles all user input to run queries.
 * 
 * @author Andrew Bell
 * 
 */
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.plugins.persistence.mySQL.EventLogDAOImpl;
import com.recursivechaos.rcbot.plugins.persistence.mySQL.QueryDAOImpl;

public class QueryListener extends ListenerAdapter<PircBotX> {
	Logger logger = LoggerFactory.getLogger(QueryListener.class);
	EventLogDAO eventlog = new EventLogDAOImpl();

	@Override
	public void onMessage(final MessageEvent<PircBotX> event) {
		if (event.getMessage().contains("!wc ")) {
			QueryDAO query = new QueryDAOImpl();
			event.respond("Word Count: "
					+ query.getWordCount(event.getMessage().substring(4), event
							.getChannel().getName()));
		}
	}

}