package com.recursivechaos.rcbot.plugins.rcrover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverRover {

	Logger logger = LoggerFactory.getLogger(RoverItem.class);
	
	public int xpos;
	public int ypos;
	
	public int heading;
	
	public boolean fluffStatus;
	public boolean pokeStatus;
	public boolean thermStatus;
	
	public RoverRover(int xpos, int ypos, int heading) {
		
		this.xpos = xpos;
		this.ypos = ypos;
		
		this.heading = heading;
		
	};
	
	public String look() {
		
		RoverRoom room = RoverRoom.getRoom(this.xpos, this.ypos);
		
		if(room.description != null) {
			return room.description;
		} else {
			return "You see nothing but blackness.";
		}
		
		
	}
	
	public void moveTo(int xpos, int ypos) {
		
		this.xpos = xpos;
		this.ypos = ypos;
		
	}
	
	public void turnLeft() {
		
		this.heading--;
		if(this.heading < 0) this.heading = 7;
		
	}

	public void turnRight() {
		
		this.heading++;
		if(this.heading > 7) this.heading = 0;
		
	}

	public boolean goForward() {
		
		int xTarget = this.xpos;
		int yTarget = this.ypos;
		
		switch(this.heading) {
		
			case 0:
			case 7:
			case 1:
				yTarget--;
				break;
				
			case 3:
			case 4:
			case 5: 
				yTarget++;
				break;
		}
		
		switch(this.heading) {
			case 1:
			case 2:
			case 3:
				xTarget++;
				break;
				
			case 5:
			case 6:
			case 7:
				xTarget--;
				break;
		}
		
		RoverRoom room = RoverRoom.getRoom(xTarget, yTarget);
		
		if (room != null) {
			this.moveTo(xTarget, yTarget);
			return true;
		} else {
			return false;
		}
			
	}
	
	public boolean goBackward() {
		
		int xTarget = this.xpos;
		int yTarget = this.ypos;
		
		switch(this.heading) {
		
			case 0:
			case 7:
			case 1:
				yTarget++;
				break;
				
			case 3:
			case 4:
			case 5: 
				yTarget--;
				break;
		}
		
		switch(this.heading) {
			case 1:
			case 2:
			case 3:
				xTarget--;
				break;
				
			case 5:
			case 6:
			case 7:
				xTarget++;
				break;
		}
		
		RoverRoom room = RoverRoom.getRoom(xTarget, yTarget);
		
		if (room != null) {
			this.moveTo(xTarget, yTarget);
			return true;
		} else {
			return false;
		}
			
	}
	
	public String getHeading() {
		
		switch(this.heading) {
		
			case 0:  return "North";
			case 1:  return "North-East";
			case 2:  return "East";
			case 3:  return "South-East";
			case 4:  return "South";
			case 5:  return "South-West";
			case 6:  return "West";
			case 7:  return "North-West";
			
			default:  return "Fucked if I know";
		}

	}

	public String getPosition() {
		
		return xpos + ", " + ypos;
		
	}
	
	public String getFluffStatus() {
		
		if(fluffStatus) {
			return "operational";
		} else {
			return "fucked up";
		}
		
	}
	
	public String getPokeStatus() {
		
		if(pokeStatus) {
			return "operational";
		} else {
			return "fucked up";
		}
	}
	
	public String getThermStatus() {
			
		if(thermStatus) {
			return "operational";
		} else {
			return "fucked up";
		}
		
	}
	
}
