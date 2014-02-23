package com.recursivechaos.rcbot.plugins.rcrover;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverRoom {

	Logger logger = LoggerFactory.getLogger(RoverRoom.class);
	
	public int xloc;
	public int yloc;
	
	public String description;
	
	public RoverRoom(int xloc, int yloc, String description) {
		
		this.xloc = xloc;
		this.yloc = yloc;
		this.description = description;
		
	}
	
	public static RoverRoom getRoom(int xpos, int ypos) {
		
		Iterator<RoverRoom> rooms = RoverWorld.roomList.iterator();
		
		boolean found = false;
		
		while(rooms.hasNext() && found == false) {
		
			RoverRoom room = rooms.next();
			
			if(room.xloc == xpos && room.yloc == ypos) {
				found = true;
				return room;
			}

		}
		
		return null;
		
	}
	
	public String look() {
		
		String result = this.description;
		
		return result;
		
	}
	
	
	
}
