package com.recursivechaos.rcbot.plugins.eventlog;

/**
 * QueryListener handles all user input to run queries.
 * 
 * @author Andrew Bell
 * 
 */
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

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
	public static long ONE_DAY = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
	@Override
	public void onMessage(final MessageEvent<PircBotX> event) {
		if (event.getMessage().contains("!wc ")) {
			QueryDAO query = new QueryDAOImpl();
			event.respond("Word Count: "
					+ query.getWordCount(event.getMessage().substring(4), event
							.getChannel().getName()));
		}
		if (event.getMessage().contains("!dwc")){
			QueryDAO query = new QueryDAOImpl();
			Timestamp now = new Timestamp(event.getTimestamp());
			Timestamp yesterday = new Timestamp(event.getTimestamp()-ONE_DAY);
			int count = query.getWordCount(event.getMessage().substring(5), event
							.getChannel().getName(),yesterday,now);
			event.respond("Daily Word Count (24 hrs): " + count);
		}
	}

}