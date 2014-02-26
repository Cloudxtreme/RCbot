package com.recursivechaos.rcbot.plugins.stoopsnoop;

/**
 * QueryListener handles all user input to run queries. Some queries will require 
 * some fiddiling to get the right arguments for the queries. A business object may
 * be able to take some of this burden.
 * 
 * @author Andrew Bell www.recursivechaos.com
 * 
 */
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.EventLogDAOImpl;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.QueryDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.log.EventLogDAO;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryDAO;

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
		if (event.getMessage().contains("!trending")){
			QueryDAO query = new QueryDAOImpl();
			Timestamp now = new Timestamp(event.getTimestamp());
			Timestamp yesterday = new Timestamp(event.getTimestamp()-ONE_DAY);
			HashMap<String,Integer> topWords = 
					query.getTopWords(5, event.getChannel().getName(), yesterday, now);
			String response = "Trending Words (24 Hours): ";
			for (Entry<String, Integer> entry : topWords.entrySet()) {
			    String block = (entry.getKey() + ":" + entry.getValue()+ " ");
				response= response + block;
			}
			event.respond(response);
		}
	}

}