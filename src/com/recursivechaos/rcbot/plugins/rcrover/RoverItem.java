package com.recursivechaos.rcbot.plugins.rcrover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoverItem {

	Logger logger = LoggerFactory.getLogger(RoverItem.class);
	
	public int xloc;
	public int yloc;
	public String description;
	public String fluffFlavor; // Fluffalize
	public String pokeFlavor;  // Pokenate
	public String thermFlavor;  // Thermabate
	
	public RoverItem (int xloc, int yloc, String description, String fluffFlavor, String pokeFlavor, String thermFlavor) {
		
		this.xloc = xloc;
		this.yloc = yloc;
		
		this.description = description;
		
		this.fluffFlavor = fluffFlavor;
		this.pokeFlavor = pokeFlavor;
		this.thermFlavor = thermFlavor;
		
	}
	
	public String fluffalize() {
		
		return this.fluffFlavor;
		
	}
	
	public String pokenate() {
		
		return this.pokeFlavor;
		
	}
	
	public String thermabate() {
		
		return this.thermFlavor;
		
	}
	
}
