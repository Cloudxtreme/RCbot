package com.recursivechaos.rcbot.plugins.eventlog;

/**
 * QueryBO handles all of the filters for the queries
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

import com.recursivechaos.rcbot.plugins.persistence.EventLog;

public class QueryBO {

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
                 if(!listOfWords.containsKey(word))
                 {                             //add word if it isn't added already
                     listOfWords.put(word, 1); //first occurance of this word
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

	public static HashMap<String, Integer> getTop(int size,HashMap<String, Integer> wordMap) {
		HashMap<String,Integer> top = new HashMap<String,Integer>();
		boolean match = false;
		int high = 0;
		int lastHigh = Integer.MAX_VALUE;
		List<String> hasHigh = new ArrayList<String>();
		do{
			do{
				HashMap<String,Integer> safeMap = new HashMap<String,Integer>(wordMap);
				Iterator<Entry<String, Integer>> it = safeMap.entrySet().iterator();
			    high = 0;
				while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        System.out.println(pairs.getKey() + " = " + pairs.getValue());
			        if (Integer.parseInt(pairs.getValue().toString()) > high && Integer.parseInt(pairs.getValue().toString()) < lastHigh){
			        	match = true;
			        	hasHigh = new ArrayList<String>();
			        	high = Integer.parseInt(pairs.getValue().toString());
			        	hasHigh.add(pairs.getKey().toString());
			        }else if (Integer.parseInt(pairs.getValue().toString()) == high){
			        	match = true;
			        	hasHigh.add(pairs.getKey().toString());
			        }
			        it.remove(); // avoids a ConcurrentModificationException
			    }
			}while(match==false);
			// Add list of words, and the current high value to the hashmap
			if(hasHigh.size()>0){
				for(int i = 0; i<hasHigh.size(); i++){
					if(top.size()<size){
						top.put(hasHigh.get(i), high);
					}
				}
			}
			lastHigh = high;
			match = false;
			
		}while(top.size()<size);
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
}
