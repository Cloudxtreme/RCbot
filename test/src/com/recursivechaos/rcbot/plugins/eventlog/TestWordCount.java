/**
 * 
 */
package com.recursivechaos.rcbot.plugins.eventlog;

import junit.framework.TestCase;

import org.junit.Test;

import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.QueryDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryDAO;

/**
 * @author andrew
 * 
 */
public class TestWordCount extends TestCase {

	@Test
	public void test() {
		QueryDAO query = new QueryDAOImpl();
		int i = query.getWordCount("moop", "#StoopTest");
		assertNotNull(i);
	}

}
