/**
 * 
 */
package es.ull.simulation.model;

/**
 * A source of simulation events. Defines at least two events: one for the moment the simulation creates
 * the object ({@link #onCreate(long)}); and another for the moment the object is destroyed ({@link #onDestroy(long)})
 * @author Ivan Castilla Rodriguez
 *
 */
public interface IEventSource extends ILoggable {
	
	/**
	 * Creates an event for the moment the simulation creates this object
	 * @param ts Timestamp when the simulation creates the object
	 * @return an event for the moment the simulation creates this object
	 */
	DiscreteEvent onCreate(final long ts);
	/**
	 * Creates an event for the moment this object is destroyed
	 * @param ts Timestamp when the simulation destroys the object
	 * @return an event for the moment this object is destroyed
	 */
	DiscreteEvent onDestroy(final long ts);

    /**
     * Informs the element that it must finish its execution and launch the destroy event. 
     */
    void notifyEnd();
    
}
