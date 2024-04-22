/**
 * 
 */
package es.ull.simulation.model.engine;

/**
 * A class capable of generating events
 * @author Iv�n Castilla
 *
 */
public interface EventSourceEngine {
    /**
     * Informs the event source that it must finish its execution. 
     */
	void notifyEnd();
}
