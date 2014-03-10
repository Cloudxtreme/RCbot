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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.pircbotx.hooks.events.MessageEvent;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.CustomQuery;
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
			// add filter on nick's name
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
		// add filter on nick's name
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
		List<EventLog> results = c.list();
		results = QueryBO.removeSpammers(results,channel);
		HashMap<String, Integer> wordMap = QueryBO.getWordMap(results);
		HashMap<String, Integer> culledMap = QueryBO.removeIgnoredWords(wordMap);
		HashMap<String, Integer> lessNicksMap = QueryBO.removeNicks(culledMap,channel);
		String[][] topList = QueryBO.getTop(NumOfRecords,lessNicksMap);
		return topList;
	}

	@Override
	public void newGenericQuery(MessageEvent<MyPircBotX> event) {
		QueryBO helper = new QueryBO();
		Timestamp now = helper.getNow(event);
		Timestamp start = helper.getDaysAgo(event,1);
		// get message
		String input = event.getMessage();
		// cut "!query"
		input = input.substring(input.indexOf(" "));
		// sanitize input (removes start/end spaces
		input = helper.sanitize(input);
		// get report name
		// Bandaid, and a bad one at that.
		input = input+ " ";
		String report = helper.sanitize(input.substring(0,input.indexOf(" ")+1));
		
		try{
			REPORT_LIST reports = REPORT_LIST.valueOf(report.toUpperCase());
			switch(reports) {
			   case TRENDING:
				   String[][] reply = getTopWords(5,event.getChannel().getName(),start,now);
				   helper.displayTrendingList(reply,5,event);
				   break;
			   default:
				   event.getBot().sendIRC().message(event.getUser().toString(), "You have failed.");
			}
		}catch(Exception e){
			//I'm not sure how I want to handle this yet
		}
	}

	@Override
	public boolean verifyUserHistory(CustomQuery customQuery) {
		boolean returnStatus = false; 
		try{
			Criteria c = getSession().createCriteria(EventLog.class);
			c.add(Restrictions.eq("channel", customQuery.getChannel()));
			c.add(Restrictions.eq("nick", customQuery.getUser()));
			c.setMaxResults(1);
			List<?> resp = c.list();
			if(resp.isEmpty()){
				returnStatus = false;
			} else {
				returnStatus = true;
			}
		}catch(Exception e){
			returnStatus = false;
		}finally{
			close();
		}
		
		return returnStatus;
	}

	@Override
	public void executeQuery(CustomQuery myQueryConfig) {
		QueryBO helper = new QueryBO();
		try{
			Criteria c = getSession().createCriteria(EventLog.class);
			// Add time restriction
			// now *should* always be not null
			Timestamp now = helper.getNow(myQueryConfig.getEvent());
			// Start on the other hand, may not be, default to 24 hours
			Timestamp start = myQueryConfig.getStart();
			if(start==null){
				// set default to 24 hours
				myQueryConfig.setStart(helper.getDaysAgo(myQueryConfig.getEvent(),1));
				start = myQueryConfig.getStart();
				myQueryConfig.setTimePeriod("hour");
				myQueryConfig.setTimeQuantity(24);
			}
			// default to results 5
			if(myQueryConfig.getResultsQuantity()<1){
				myQueryConfig.setResultsQuantity(5);
			}
			//Timestamp start = helper.getDaysAgo(myQueryConfig.getEvent(),TEMP_DAYS);
			c.add(Restrictions.between("sqltimestamp",start,now));
			switch(myQueryConfig.getReport()) {
			   case TRENDING:
				   String response = "TRENDING:";
				   // Set channel (default)
				   c.add(Restrictions.like("channel", myQueryConfig.getChannel()));
				   // Set nick (optional)
				   if(myQueryConfig.getUser()!=null){
					   c.add(Restrictions.like("nick", myQueryConfig.getUser()));
					   response = response + " USER: " + myQueryConfig.getUser();
				   } else {
					   response = response + " CHANNEL: " + myQueryConfig.getChannel();
				   }
				   response = response + " TIME: " + myQueryConfig.getTimeQuantity() + " " 
						   + myQueryConfig.getTimePeriod() + ":";
				   //response = response + " TOP "  + TEMP_RECORDS + ": ";
				   
				   // Run the query
				   @SuppressWarnings("unchecked")
				   List<EventLog> list = (List<EventLog>)c.list();
				   list= QueryBO.removeSpammers(list,myQueryConfig.getChannel());
				   HashMap<String, Integer> wordMap = QueryBO.getWordMap(list);
				   wordMap = QueryBO.removeIgnoredWords(wordMap);
				   wordMap = QueryBO.removeNicks(wordMap,myQueryConfig.getChannel());
				   String[][] topList = QueryBO.getTop(myQueryConfig.getResultsQuantity(),wordMap);
				   // Add records
				   for (int i = 0; i<myQueryConfig.getResultsQuantity();i++){
						//response = response + (topList[i][0] + "-" + topList[i][1]+ " ");
					   response = response + " " + (i+1) + ")" + topList[i][0] + "[" + topList[i][1] + "]";
					}
				   myQueryConfig.getEvent().respond(response);
				   break;
			   case WORDCOUNT:
				   break;
			   case TOPUSER:
				   break;
			   case LASTMENTION:
				   break;
		}
		}catch(Exception e){
			// No errors are thrown here, but there's a lot going on
		}finally{
			close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<EventLog> getPreviousMessages(MessageEvent<MyPircBotX> event, int qty) {
		List<EventLog> results = new ArrayList<EventLog>();
		try{
			Criteria c = getSession().createCriteria(EventLog.class);
			c.add(Restrictions.like("channel", event.getChannel().getName()));
			c.add(Restrictions.like("nick", event.getUser().getNick()));
			c.addOrder(Order.desc("sqltimestamp"));
			c.setMaxResults(qty);
			results = c.list();
		}catch(Exception e){
			
		}finally{
			close();
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	public List<EventLog> getPreviousHourMessages(MessageEvent<MyPircBotX> event, int qty) {
		List<EventLog> results = new ArrayList<EventLog>();
		try{
			Criteria c = getSession().createCriteria(EventLog.class);
			c.add(Restrictions.like("channel", event.getChannel().getName()));
			c.add(Restrictions.like("nick", event.getUser().getNick()));
			c.add(Restrictions.gt("sqltimestamp", (event.getTimestamp()-QueryBO.HOUR)));
			c.addOrder(Order.desc("sqltimestamp"));
			c.setMaxResults(qty);
			results = c.list();
		}catch(Exception e){
			
		}finally{
			close();
		}
		return results;
	}
	

}
