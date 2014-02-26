package com.recursivechaos.rcbot.plugins.persistence.hibernate.dao;

/**
 * QueryDAOImpl provides the interface for the bot to perform queries. All queries should be
 * considered "safe" for the bot to call without fear of "cross-contaminated" data, or 
 * un-reasonable queries.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.EventLog;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryBO;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryDAO;

public class QueryDAOImpl extends DAO implements QueryDAO {

	@Override
	public int getWordCount(String searchTerm, String channel) {
		searchTerm = searchTerm.toLowerCase();
		Criteria c = getSession().createCriteria(EventLog.class);
		// add filter on user's name
		c.add(Restrictions.ilike("message", "%" + searchTerm + "%"));
		// Restricts only to the channel called from.
		c.add(Restrictions.eq("channel", channel));
		// Technically, we're only returning the amount of messages that contain
		// the word, not the total count of the word
		// return c.list().size();

		// Fetch list
		@SuppressWarnings("unchecked")
		List<EventLog> records = c.list();
		records = QueryBO.removeStartsWith(records, "!");
		return QueryBO.wordCount(records, searchTerm);
	}

	public int getWordCount(String searchTerm, String channel, Timestamp start, Timestamp end){
		searchTerm = searchTerm.toLowerCase();
		Criteria c = getSession().createCriteria(EventLog.class);
		// add filter on user's name
		c.add(Restrictions.ilike("message", "%" + searchTerm + "%"));
		// Restricts only to the channel called from.
		c.add(Restrictions.eq("channel", channel));
		// Restricts date
		c.add(Restrictions.between("sqltimestamp", start, end));
		// Fetch list
		@SuppressWarnings("unchecked")
		List<EventLog> records = c.list();
		records = QueryBO.removeStartsWith(records, "!");
		return QueryBO.wordCount(records, searchTerm);
	}
	
	@SuppressWarnings({ "unchecked" })
	public HashMap<String, Integer> getTopWords(int NumOfRecords, String channel, Timestamp start, Timestamp end){
		// create criteria
		Criteria c = getSession().createCriteria(EventLog.class);
		c.add(Restrictions.eq("channel", channel));
		c.add(Restrictions.between("sqltimestamp", start, end));
		HashMap<String, Integer> wordMap = QueryBO.getWordMap((List<EventLog>)c.list());
		HashMap<String, Integer> culledMap = QueryBO.removeIgnoredWords(wordMap);
		HashMap<String, Integer> topList = QueryBO.getTop(5,culledMap);
		return topList;
	}
}
