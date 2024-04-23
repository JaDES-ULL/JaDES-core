/**
 * 
 */
package es.ull.simulation.model.engine;

/**
 * A class capable of generating events
 * @author Iván Castilla Rodríguez
 *
 */
public interface IEventSourceEngine {
    /**
     * Informs the event source that it must finish its execution. 
     */
	void notifyEnd();
}
