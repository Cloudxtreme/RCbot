package com.recursivechaos.rcbot.plugins.eventlog;

/**
 * QueryBO handles all of the filters for the queries
 * 
 * @author Andrew Bell
 */
import java.util.List;

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
}
