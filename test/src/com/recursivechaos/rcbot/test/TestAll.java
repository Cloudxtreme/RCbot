package com.recursivechaos.rcbot.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.recursivechaos.rcbot.plugins.dadjokes.TestDadJoke;
import com.recursivechaos.rcbot.plugins.eventlog.TestWordCount;

public class TestAll {
	public static Test suite() {
		TestSuite suite = new TestSuite("All Tests");
		suite.addTestSuite(TestDadJoke.class);
		suite.addTestSuite(TestWordCount.class);

		return suite;

	}
}
