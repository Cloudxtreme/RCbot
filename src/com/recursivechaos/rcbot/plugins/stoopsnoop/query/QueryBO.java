package com.recursivechaos.rcbot.plugins.stoopsnoop.query;

/**
 * QueryBO handles all of the filters for the queries. 
 * For the record, I don't like that it calls DAO, but until I expand my 
 * data mapping, this is what it's gonig to be :)
 * 
 * @author Andrew Bell
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.DAO;
import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.DictWordDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.DictWord;
import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.EventLog;

public class QueryBO extends DAO{

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
                 if(!listOfWords.containsKey(word))
                 {                             //add word if it isn't added already
                	 if (!word.isEmpty()){
                		 listOfWords.put(word, 1); //first occurance of this word
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
	
	private static <K, V extends Comparable<V>> List<Entry<K, V>> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> entries = new ArrayList<Entry<K, V>>(map.entrySet());
		Collections.sort(entries, new ByValue<K, V>());
		return entries;
	}

	private static class ByValue<K, V extends Comparable<V>> implements Comparator<Entry<K, V>> {
		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
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
		List<String> nicks = query.list();
		for (String word : nicks) {
			// remove by key :)
			culledMap.remove(word.toLowerCase());
		}
		return culledMap;
	}
}
