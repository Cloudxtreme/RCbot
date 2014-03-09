package com.recursivechaos.rcbot.plugins.stoopsnoop.query;

/**
 * QueryBO handles all of the filters for the queries. 
 * For the record, I don't like that it calls DAO, but until I expand my 
 * data mapping, this is what it's gonig to be :)
 * 
 * @author Andrew Bell
 */
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.pircbotx.hooks.events.MessageEvent;

import com.recursivechaos.rcbot.bot.object.MyPircBotX;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.DAO;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.DictWordDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.DictWord;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.EventLog;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.NickFilterGroup;

public class QueryBO extends DAO{
	public static final long SECOND 	= TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);
	public static final long MINUTE 	= TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
	public static final long HOUR 		= TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
	public static final long DAY 		= TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
	public static final long WEEK 		= 7 	* DAY;
	public static final long MONTH		= 39	* DAY;
	public static final long YEAR		= 365	* DAY;
	/**
	 * filterChannel returns records matching the channel filter.
	 * 
	 * @param records
	 *            EventLog list to match from
	 * @param channel
	 *            Requested channel name
	 * @return list of records from channel
	 */
	public static List<EventLog> filterChannel(List<EventLog> records,
			String channel) {

		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).getChannel() != channel) {
				records.remove(i);
			}
		}

		return records;
	}

	/**
	 * removeStartsWith searches, and removes, from the provided record list,
	 * any messages starting with the string filter. Useful from removing bot
	 * commands.
	 * 
	 * @param records
	 *            EventLog list to remove from
	 * @param filter
	 *            checked against every line to see
	 * @return
	 */
	public static List<EventLog> removeStartsWith(List<EventLog> records,
			String filter) {
		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).getMessage().startsWith(filter)) {
				records.remove(i);
				// Since we are removing a record, the index is now wrong. This
				// whole loop is
				// bad, and I feel bad.
				i--;
			}
		}
		return records;

	}

	/**
	 * wordCount search for all instances of the search term. This word count
	 * will not necessarily hit on the same words as whatever persistence
	 * implementation's "like" query will return. Not likely to make a huge
	 * impact though.
	 * 
	 * @param records
	 * @param searchTerm
	 * @return
	 */
	public static int wordCount(List<EventLog> records, String searchTerm) {
		searchTerm = searchTerm.toLowerCase();
		int count = 0;
		for (int i = 0; i < records.size(); i++) {
			String message = records.get(i).getMessage().toLowerCase();
			int index = -1;
			do {
				index = message.indexOf(searchTerm);

				if (index != -1) {
					message = message.substring(index + searchTerm.length());
					count++;
				}
			} while (index != -1);
		}
		return count;
	}

	public static HashMap<String, Integer> getWordMap(List<EventLog> list) {
        HashMap<String, Integer> listOfWords = new HashMap<String, Integer>();
        for(int i = 0; i<list.size(); i++){
        	String[] arr = list.get(i).getMessage().split(" ");    
        	 for ( String word : arr) {
        		 int countWord = 0;
        		 word = word.toLowerCase();
        		 // remove punctuation
        		 word = removePunctuation(word,".");
        		 word = removePunctuation(word,"?");
        		 // effectively removes all bot commands
        		 word = removePunctuation(word,"!");
        		 word = removePunctuation(word,",");
        		 word = removePunctuation(word,"http://");
        		 word = removePunctuation(word,"-");
        		 word = removePunctuation(word,"(");
        		 word = removePunctuation(word,":");
        		 word = removePunctuation(word,"*");
                 
                 // Checks for plural words, and will add them under the non-plural form if available
                 // Will not work if the singular form is not in the hashmap yet, so is
                 // not accurate.
                 if((word.endsWith("s"))&&(listOfWords.containsKey(word.substring(0,word.length()-1))))
                 {
                	 String unplural = word.substring(0,word.length()-1);
                     countWord = listOfWords.get(unplural) + 1; //get current count and increment
                     //now put the new value back in the HashMap
                     listOfWords.remove(unplural); //first remove it (can't have duplicate keys)
                     listOfWords.put(unplural, countWord); //now put it back with new value
                 }else if(!listOfWords.containsKey(word)){//add word if it isn't added already
                	 if (!word.isEmpty()){
                		 listOfWords.put(word, 1); //first occurrence of this word
                	 }
                 }
                 else
                 {
                     countWord = listOfWords.get(word) + 1; //get current count and increment
                     //now put the new value back in the HashMap
                     listOfWords.remove(word); //first remove it (can't have duplicate keys)
                     listOfWords.put(word, countWord); //now put it back with new value
                 }
        	  }
        }
        return listOfWords; //return the HashMap you made of distinct words
	}

	private static String removePunctuation(String word, String punc) {
		while(word.contains(punc)==true){
			 word = word.substring(0,word.indexOf(punc));
		 }
		return word;
	}

	public static String[][] getTop(int size,HashMap<String, Integer> wordMap) {
		// Create a 2D string array, so order does not get swapped.
		String[][] 	top = new String [size][2];
		
		// iterate size
		for (int i = 0; i<size;i++){
			String 		tempKey = "";
			int			tempVal = 0;

			// iterate through word map to find highest value
			for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
			    
				if( entry.getValue() > tempVal){
			    	tempVal = entry.getValue();
			    	tempKey = entry.getKey();
			    }
			}
			top[i][0]=tempKey;
			top[i][1]=Integer.toString(tempVal);
			wordMap.remove(tempKey);
		}
		return top;
	}

	public static HashMap<String, Integer> removeIgnoredWords(HashMap<String, Integer> wordMap) {
		
		// load dictionary
		DictWordDAO dictionary = new DictWordDAOImpl();
		// get list of ignored words
		List<DictWord> ignoreList = (List<DictWord>) dictionary.getIgnoredWordList();
		// iterate through ignoreList
		for (DictWord word : ignoreList) {
			// remove by key :)
			wordMap.remove(word.getWord().toLowerCase());
		}
		return wordMap;
	}

	public static HashMap<String, Integer> removeNicks(
			HashMap<String, Integer> culledMap, String channel) {
		// fetch distinct list
		Query query = getSession().createQuery("SELECT DISTINCT nick " +
				"FROM EventLog " +
				"WHERE channel = ?");
		query.setString(0, channel);
		// remove from list
		@SuppressWarnings("unchecked")
		List<String> nicks = query.list();
		for (String word : nicks) {
			// remove by key :)
			culledMap.remove(word.toLowerCase());
		}
		return culledMap;
	}

	public Timestamp getNow(MessageEvent<MyPircBotX> event) {
		return new Timestamp(event.getTimestamp());
	}

	public Timestamp getDaysAgo(MessageEvent<MyPircBotX> event, int i) {
		return new Timestamp(event.getTimestamp()-(DAY*i));
	}

	public void displayTrendingList(String[][] topWords, int count,
			MessageEvent<MyPircBotX> event) {
		String response = "Trending Words (24 Hours): ";
		//for (Entry<String, Integer> entry : topWords.entrySet()) {
		for (int i = 0; i<count;i++){
			String block = (topWords[i][0] + ":" + topWords[i][1]+ " ");
			response= response + block;
		}
		event.respond(response);
	}

	public Timestamp getHoursFromNow(MessageEvent<MyPircBotX> event, int hours) {
		return new Timestamp(event.getTimestamp()+(hours*HOUR));
	}

	public static List<EventLog> removeSpammers(
			List<EventLog> messageList, String channel) {
		List<NickFilterGroup> banList = getBannedUsers(channel);
		
				Iterator<EventLog> i = messageList.iterator();
				while (i.hasNext()) {
					EventLog message = i.next(); // must be called before you can call i.remove()
					for (NickFilterGroup ban : banList){
						if(message.getNick().equals(ban.getNick())){
							 i.remove();
						}
					}
				}
		return messageList;
	}

	@SuppressWarnings("unchecked")
	private static List<NickFilterGroup> getBannedUsers(String channel) {
		java.util.Date today = new java.util.Date();
		Timestamp start = new Timestamp(today.getTime());
		List<NickFilterGroup> results = new ArrayList<NickFilterGroup>();
		try{
			Criteria c = getSession().createCriteria(NickFilterGroup.class);
			c.add(Restrictions.eq("channel", channel));
			c.add(Restrictions.gt("end",start));
			results = (List<NickFilterGroup>) c.list();
		}catch(Exception e){
			
		}finally{
			close();
		}
		return results;
	}

	public Timestamp getPeriodsAgo(int timeQuantity, String timePeriod, MessageEvent<MyPircBotX> event) {
		String search = stripTimePeriod(timePeriod);
		switch(search){
		case "hour":
			return new Timestamp(event.getTimestamp()-(HOUR*timeQuantity));
		case "day":
			return new Timestamp(event.getTimestamp()-(DAY*timeQuantity));
		case "week":
			return new Timestamp(event.getTimestamp()-(WEEK*timeQuantity));
		case "month":
			return new Timestamp(event.getTimestamp()-(MONTH*timeQuantity));
		case "year":
			return new Timestamp(event.getTimestamp()-(YEAR*timeQuantity));
		default:
			return null;
		}
	}

	/**
	 * Sanitizes, and removes any tailing 's'
	 * @param timePeriod
	 * @return
	 */
	private String stripTimePeriod(String timePeriod) {
		timePeriod = sanitize(timePeriod);
		if(timePeriod.endsWith("s")){
			timePeriod = timePeriod.substring(0, timePeriod.length()-1);
		}
		return timePeriod;
	}
	
	/**
	 * Removes start/end spaces, and forces lowercase
	 * @param dirty, dirty input
	 * @return squeaky clean output
	 */
	public String sanitize(String input) {
		input = input.trim();
		input = input.toLowerCase();
		return input;
	}
}
