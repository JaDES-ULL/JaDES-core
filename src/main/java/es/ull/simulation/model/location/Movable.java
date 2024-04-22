/**
 * 
 */
package com.ull.simulation.model.location;

/**
 * An object that can move from one location to another
 * @author Iv�n Castilla Rodr�guez
 *
 */
public interface Movable extends Located {
	/**
	 * Sets the current location of the object
	 * @param location The current location of the object 
	 */
	void setLocation(final Location location);
	void notifyLocationAvailable(final Location location);
}
