package com.recursivechaos.rcbot.plugins.rcrover;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverListener extends ListenerAdapter<PircBotX> {
	
	Logger logger = LoggerFactory.getLogger(RoverListener.class);
	
	static RoverWorld roverWorld = new RoverWorld();
	
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
					
			if(event.getMessage().contains("look")) {
				
				event.respond(this.roverWorld.rover.look());
				
			} else if(event.getMessage().contains("forward")) {
				
				boolean result = this.roverWorld.rover.goForward();
				if(!result) {
					event.respond("Sorry boss, can't do that.");
				}
				event.respond(this.roverWorld.rover.look());
				
			} else if (event.getMessage().contains("backward")) {
				
				boolean result = this.roverWorld.rover.goBackward();
				if(!result) {
					event.respond("Sorry boss, can't do that.");
				}
				event.respond(this.roverWorld.rover.look());
				
			} else if (event.getMessage().contains("turn left")) {
				
				this.roverWorld.rover.turnLeft();
				event.respond("I am now facing " + this.roverWorld.rover.getHeading());
				
			} else if (event.getMessage().contains("turn right")) {
				
				this.roverWorld.rover.turnRight();
				event.respond("I am now facing " + this.roverWorld.rover.getHeading());
				
			} else if (event.getMessage().contains("get heading")) {
				
				event.respond("I am currently facing " + this.roverWorld.rover.getHeading());
				
			}
			
			
			
	
			
			
		}
		
	}

}



