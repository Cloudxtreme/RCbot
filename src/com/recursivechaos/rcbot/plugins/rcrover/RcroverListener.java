package com.recursivechaos.rcbot.plugins.rcrover;

/**
 * RcroverListener is Keith's problem
 * 
 * @author
 */
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RcroverListener extends ListenerAdapter<PircBotX> {
	Logger logger = LoggerFactory.getLogger(RcroverListener.class);
	// Initialize any object you want to persist
	
	/**
	 * This method will "hit" every time a message (not an action) is sent
	 * PircBotX has a lot of information about what you can do with an event object
	 */
	@Override
	public void onMessage(final MessageEvent<PircBotX> event) {
		// Call your game logic
		if(event.getMessage().startsWith("!helloworld")){
			event.respond("Hello World!");
		}
	}

}
