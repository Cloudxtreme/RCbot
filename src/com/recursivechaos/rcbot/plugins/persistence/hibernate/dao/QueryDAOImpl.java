package com.recursivechaos.rcbot.plugins.persistence.hibernate.dao;

/**
 * QueryDAOImpl provides the interface for the bot to perform queries. All queries should be
 * considered "safe" for the bot to call without fear of "cross-contaminated" data, or 
 * un-reasonable queries.
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.pircbotx.hooks.events.MessageEvent;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.EventLog;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryBO;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryDAO;

public class QueryDAOImpl extends DAO implements QueryDAO {
	public static enum REPORT_LIST {TRENDING};
	
	@SuppressWarnings("unchecked")
	@Override
	public int getWordCount(String searchTerm, String channel) {
		searchTerm = searchTerm.toLowerCase();
		List<EventLog> records = new ArrayList<EventLog>();
		try{
			Criteria c = getSession().createCriteria(EventLog.class);
			// add filter on user's name
			c.add(Restrictions.ilike("message", "%" + searchTerm + "%"));
			// Restricts only to the channel called from.
			c.add(Restrictions.eq("channel", channel));
			// Fetch list
			records = (List<EventLog>)c.list();
		}catch(Exception e){
			
		}finally{
			close();
		}
		return QueryBO.wordCount(records, searchTerm);
	}
	
	@SuppressWarnings("unchecked")
	public int getWordCount(String searchTerm, String channel, Timestamp start, Timestamp end){
		searchTerm = searchTerm.toLowerCase();
		List<EventLog> records = new ArrayList<EventLog>();
		Criteria c = getSession().createCriteria(EventLog.class);
		// add filter on user's name
		c.add(Restrictions.ilike("message", "%" + searchTerm + "%"));
		// Restricts only to the channel called from.
		c.add(Restrictions.eq("channel", channel));
		// Restricts date
		c.add(Restrictions.between("sqltimestamp", start, end));
		// Fetch list
		records = c.list();
		records = QueryBO.removeStartsWith(records, "!");
		return QueryBO.wordCount(records, searchTerm);
	}
	
	@SuppressWarnings( "unchecked" )
	public String[][] getTopWords(int NumOfRecords, String channel, Timestamp start, Timestamp end){
		// create criteria
		Criteria c = getSession().createCriteria(EventLog.class);
		c.add(Restrictions.eq("channel", channel));
		c.add(Restrictions.between("sqltimestamp", start, end));
		HashMap<String, Integer> wordMap = QueryBO.getWordMap((List<EventLog>)c.list());
		HashMap<String, Integer> culledMap = QueryBO.removeIgnoredWords(wordMap);
		HashMap<String, Integer> lessNicksMap = QueryBO.removeNicks(culledMap,channel);
		String[][] topList = QueryBO.getTop(NumOfRecords,lessNicksMap);
		return topList;
	}

	@Override
	public void newGenericQuery(MessageEvent<MyPircBotX> event) {
		QueryBO helper = new QueryBO();
		Timestamp now = helper.getNow(event);
		Timestamp yesterday = helper.getDaysAgo(event,1);
		// get message
		String input = event.getMessage();
		// cut "!query"
		input = input.substring(input.indexOf(" "));
		// sanitize input (removes start/end spaces
		input = sanitize(input);
		// get report name
		// Bandaid, and a bad one at that.
		input = input+ " ";
		String report = sanitize(input.substring(0,input.indexOf(" ")+1));
		
		try{
			REPORT_LIST reports = REPORT_LIST.valueOf(report.toUpperCase());
			switch(reports) {
			   case TRENDING:
				   String[][] reply = getTopWords(5,event.getChannel().getName(),yesterday,now);
				   helper.displayTrendingList(reply,5,event);
				   break;
			   default:
				   event.getBot().sendIRC().message(event.getUser().toString(), "You have failed.");
			}
		}catch(Exception e){
			//I'm not sure how I want to handle this yet
		}
	}

	/**
	 * Removes start/end spaces, and forces lowercase
	 * @param dirty, dirty input
	 * @return squeaky clean output
	 */
	private String sanitize(String input) {
		while(input.startsWith(" ")){
			input = input.substring(1);
		}
		while(input.endsWith(" ")){
			input = input.substring(0,input.length()-1);
		}
		input = input.toLowerCase();
		return input;
	}
}
