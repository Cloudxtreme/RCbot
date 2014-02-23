package com.recursivechaos.rcbot.plugins.rcrover;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverListener extends ListenerAdapter<PircBotX> {
	
	Logger logger = LoggerFactory.getLogger(RoverListener.class);
	
	RoverWorld roverWorld = new RoverWorld();
	
	/**
	 * This method will "hit" every time a message (not an action) is sent
	 * PircBotX has a lot of information about what you can do with an event object
	 */
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void onMessage(final MessageEvent<PircBotX> event) {
		// Call your game logic
		if(event.getMessage().startsWith("!rover")){
			
			if(event.getMessage().contains("forward")) {
				boolean result = this.roverWorld.rover.goForward();
				if(!result) {
					event.respond("Sorry boss, can't do that.");
				}
				
			} else if (event.getMessage().contains("backward")) {
				boolean result = this.roverWorld.rover.goBackward();
				if(!result) {
					event.respond("Sorry boss, can't do that.");
				}
			}
			
	
			
			event.respond(this.roverWorld.rover.look());
		}
		
	}

}



