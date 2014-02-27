package com.recursivechaos.rcbot.plugins.stoopsnoop.query;

import junit.framework.TestCase;

import com.recursivechaos.rcbot.plugins.persistence.hibernate.dao.DictWordDAOImpl;

public class TestQueryDAO extends TestCase{
	

	public void test() {
		DictWordDAO dict = new DictWordDAOImpl();
		assertNotNull(dict.getIgnoredWordList());
	}

}
