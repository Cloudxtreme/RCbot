package com.recursivechaos.rcbot.plugins.stoopsnoop;

/**
 * QueryListener handles all nick input to run queries. Some queries will require 
 * some fiddiling to get the right arguments for the queries. A business object may
 * be able to take some of this burden.
 * 
 * @author Andrew Bell www.recursivechaos.com
 * 
 */
import java.sql.Timestamp;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.EventLogDAOImpl;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.QueryDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.log.EventLogDAO;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.CustomQuery;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryBO;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryDAO;

public class QueryListener extends ListenerAdapter<MyPircBotX> {
	Logger logger = LoggerFactory.getLogger(QueryListener.class);
	EventLogDAO eventlog = new EventLogDAOImpl();
	QueryBO helper = new QueryBO();
	public void onMessage(final MessageEvent<MyPircBotX> event) {
		// Big bad mega-query
		if (event.getMessage().startsWith("!query")){
			//QueryDAO query = new QueryDAOImpl();
			CustomQuery myQueryConfig = new CustomQuery(event);
			QueryDAO query = new QueryDAOImpl();
			query.executeQuery(myQueryConfig);
			//query.newGenericQuery(event);
		}
		if (event.getMessage().startsWith("!wc")) {
			QueryDAO query = new QueryDAOImpl();
			event.respond("Word Count: "
					+ query.getWordCount(event.getMessage().substring(4), event
							.getChannel().getName()));
		}
		if (event.getMessage().startsWith("!dwc")){
			QueryDAO query = new QueryDAOImpl();
			Timestamp now = helper.getNow(event);
			Timestamp yesterday = helper.getDaysAgo(event, 1);
			int count = query.getWordCount(event.getMessage().substring(5), event
							.getChannel().getName(),yesterday,now);
			event.respond("Daily Word Count (24 hrs): " + count);
		}
		if (event.getMessage().startsWith("!trending")){
			int count = 5;
			QueryDAO query = new QueryDAOImpl();
			Timestamp now = helper.getNow(event);
			Timestamp yesterday = helper.getDaysAgo(event, 1);
			String[][] topWords = 
					query.getTopWords(count, event.getChannel().getName(), yesterday, now);
			helper.displayTrendingList(topWords,count,event);
		}
	}

}