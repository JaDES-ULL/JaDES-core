/**
 * 
 */
package es.ull.simulation.model;

import es.ull.simulation.utils.Output;

/**
 * Indicates that an object can print debug messages
 * @author Iván Castilla Rodríguez
 *
 */
public interface IDebuggable {
	/** Output for printing debug and error messages */
    Output out = new Output();

    /**
	 * Prints a debug message.
	 * @param message Message to be printed
	 */
	default void debug(final String message) {
		out.debug(message);
	}

	/**
	 * Prints an error message.
	 * @param message Message to be printed
	 */
    default void error(final String message) {
		out.error(message);
	}
    
    /**
     * Checks if debug mode is enabled.
     * @return True if debug is enabled; false in other case
     */
	default boolean isDebugEnabled() {
		return out.isDebugEnabled();
	}

	/**
	 * Sets the debug mode.
	 * @param enabled True to enable debug mode; false to disable it
	 */
	default void setDebugEnabled(boolean enabled) {
		out.setDebugEnabled(enabled);
	}
    
}
