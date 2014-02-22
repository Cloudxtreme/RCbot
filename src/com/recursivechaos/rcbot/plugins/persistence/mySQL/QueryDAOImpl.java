package com.recursivechaos.rcbot.plugins.persistence.mySQL;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.recursivechaos.rcbot.plugins.eventlog.QueryBO;
import com.recursivechaos.rcbot.plugins.eventlog.QueryDAO;
import com.recursivechaos.rcbot.plugins.persistence.DAO;
import com.recursivechaos.rcbot.plugins.persistence.EventLog;

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

}
