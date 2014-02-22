/**
 * 
 */
package com.recursivechaos.rcbot.plugins.dadjokes;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author andrew
 * 
 */
public class TestDadJoke extends TestCase {



	@Test
	public void test() {
		DadJokeListener dadJoke = new DadJokeListener();
		// String resp = dadJoke.parseDadJoke("I'm old", "botname");
		// assertNotNull(resp);
		
//		String resp = dadJoke.parseDadJoke("That mold", "botname");
//		assertNull(resp);
		
		String resp = dadJoke.parseDadJoke("I'm old", "botname");
		assertEquals(resp,"Hi old, I'm botname.");
	}

}
