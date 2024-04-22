/**
 * 
 */
package com.ull.simulation.model.location;

import com.ull.functions.AbstractTimeFunction;

/**
 * A location representing a specific place.
 * 
 * @author Iv�n Castilla Rodr�guez
 *
 */
public class Node extends Location {
	/**
	 * Creates a node with capacity constrains.
	 * @param description A brief description of the location
	 * @param delayAtExit The time that it takes to exit (or go through) the location
	 * @param capacity Total capacity of the location
	 */
	public Node(String description, AbstractTimeFunction delayAtExit, int capacity) {
		super(description, delayAtExit, capacity);
	}
	
	/**
	 * Creates a node with capacity constrains.
	 * @param description A brief description of the location
	 * @param delayAtExit The time that it takes to exit (or go through) the location
	 * @param capacity Total capacity of the location
	 */
	public Node(String description, long delayAtExit, int capacity) {
		super(description, delayAtExit, capacity);
	}

	/**
	 * Creates a node with no capacity constrains.
	 * @param description A brief description of the location
	 * @param delayAtExit The time that it takes to exit (or go through) the location
	 */
	public Node(String description, AbstractTimeFunction delayAtExit) {
		super(description, delayAtExit);
	}
	
	/**
	 * Creates a node with no capacity constrains.
	 * @param description A brief description of the location
	 * @param delayAtExit The time that it takes to exit (or go through) the location
	 */
	public Node(String description, long delayAtExit) {
		super(description, delayAtExit);
	}
	
	/**
	 * Creates a node with capacity constrains and no time to exit.
	 * @param description A brief description of the location
	 * @param capacity Total capacity of the location
	 */
	public Node(String description, int capacity) {
		super(description, capacity);
	}
	
	/**
	 * Creates a node with no capacity constrains and no time to exit.
	 * @param description A brief description of the location
	 */
	public Node(String description) {
		super(description);
	}
	
	@Override
	public Location getLocation() {
		return this;
	}

}
