package com.recursivechaos.rcbot.plugins.eventlog;
/**
 * QueryDAO provides a common interface for queries to the database. QueryDAOImpl
 * should be in the .persistance.hibernate.QueryDAOImpl.
 * 
 * @author Andrew Bell
 * 
 */
public interface QueryDAO {

	public int getWordCount(String substring);
}
