package com.recursivechaos.rcbot.plugins.stoopsnoop.query;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.QueryDAOImpl;

public class TestDictWordDAO extends TestCase{
	public static long ONE_DAY = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

	public void test() {
		QueryDAO q = new QueryDAOImpl();
		Timestamp end = new Timestamp(System.currentTimeMillis());
		Timestamp start = new Timestamp(end.getTime()-(ONE_DAY));
		String[][] resp = q.getTopWords(10, "#reddit-stlouis", start, end);
		assertNotNull(resp);
	}
	


}
