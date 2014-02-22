package com.recursivechaos.rcbot.plugins.eventlog;

/**
 * QueryDAO provides a common interface for queries to the database.
 * QueryDAOImpl should be in the .persistance.hibernate.QueryDAOImpl.
 * 
 * @author Andrew Bell
 * 
 */
public interface QueryDAO {

	/**
	 * getWordCount will search the records for the total number of strings like
	 * the searchTerm on the particular channel.
	 * 
	 * @param searchTerm
	 *            String, word or phrase, to query records
	 * @param channel
	 *            String name of channel to search on
	 * @return integer of total word count
	 */
	public int getWordCount(String searchTerm, String channel);
}
