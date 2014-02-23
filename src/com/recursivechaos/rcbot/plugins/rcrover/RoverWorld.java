package com.recursivechaos.rcbot.plugins.rcrover;

import java.util.ArrayList;

public class RoverWorld {

	RoverRover rover = new RoverRover(0, 0, 0);

	static ArrayList<RoverRoom> roomList = new ArrayList<RoverRoom>();
	
	public RoverWorld() {
		
		roomList.add(new RoverRoom(0, 0, "test"));
		roomList.add(new RoverRoom(0, -1, "test again"));

		System.out.println("Desc: " + this.rover.look());
		
	}
	
}