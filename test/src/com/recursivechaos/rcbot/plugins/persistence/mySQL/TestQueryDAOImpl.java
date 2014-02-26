package com.recursivechaos.rcbot.plugins.persistence.mySQL;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.QueryDAOImpl;
import com.recursivechaos.rcbot.plugins.stoopsnoop.query.QueryDAO;

import junit.framework.TestCase;

public class TestQueryDAOImpl extends TestCase{
	public static long ONE_DAY = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

	public void test() {
		QueryDAO query = new QueryDAOImpl();
		Timestamp end = new Timestamp(System.currentTimeMillis());
		Timestamp start = new Timestamp(end.getTime()-ONE_DAY);
		HashMap<String, Integer> results = query.getTopWords(5, "#reddit-stlouis", start, end);
		assertEquals(results.size(),5);
	}

}
