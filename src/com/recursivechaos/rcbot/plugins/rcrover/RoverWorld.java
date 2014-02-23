package com.recursivechaos.rcbot.plugins.rcrover;

import java.util.ArrayList;

public class RoverWorld {

	RoverRover rover = new RoverRover(0, 0, 0);

	static ArrayList<RoverRoom> roomList = new ArrayList<RoverRoom>();
	
	public RoverWorld() {
		
		roomList.add(new RoverRoom(0, 0, "I am sitting in the small, shallow crater that was formed when my landing craft touched down.  Around me, as far as my camera can see, is a flat expanse of dirt.  Jesus Christ this is depressing.  I hope Soupmeister creates some more content for me soon."));
		roomList.add(new RoverRoom(0, -1, "I am on flat ground, there is really nothing to see here... except, of course, from that SUPER-AWESOME OBJECT which soupy is about to code into the world!"));

		System.out.println("Desc: " + this.rover.look());
		
	}
	
}