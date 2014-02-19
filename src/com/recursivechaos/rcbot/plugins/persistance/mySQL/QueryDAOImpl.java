package com.recursivechaos.rcbot.plugins.persistance.mySQL;
/**
 * QueryDAOImpl holds all of the query type logic that can be preformed
 * on the eventLog database.
 * 
 * @author Andrew Bell
 * 
 */
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.recursivechaos.rcbot.plugins.eventlog.QueryDAO;
import com.recursivechaos.rcbot.plugins.persistance.DAO;
import com.recursivechaos.rcbot.plugins.persistance.EventLog;

public class QueryDAOImpl extends DAO implements QueryDAO{

	@Override
	public int getWordCount(String searchTerm) {
		Criteria c = getSession().createCriteria(EventLog.class);
		// add filter on user's name
		c.add(Restrictions.ilike("message", "%" + searchTerm + "%"));
		// Technically, we're only returning the amount of messages that contain
		// the word, not the total count of the word
		return c.list().size();
	}

}
