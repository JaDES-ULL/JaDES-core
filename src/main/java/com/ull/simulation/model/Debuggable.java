/**
 * 
 */
package com.ull.simulation.model;

/**
 * Indicates that an object can print debug messages
 * @author Iv�n Castilla Rodr�guez
 *
 */
public interface Debuggable {
	/**
	 * Prints a debug message.
	 * @param message Message to be printed
	 */
    void debug(final String message);

	/**
	 * Prints an error message.
	 * @param message Message to be printed
	 */
    void error(final String message);
    
    /**
     * Checks if debug mode is enabled.
     * @return True if debug is enabled; false in other case
     */
	boolean isDebugEnabled();
    
}
