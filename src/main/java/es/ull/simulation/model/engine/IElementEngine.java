/**
 * 
 */
package es.ull.simulation.model.engine;

import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.flow.IFlow;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public interface IElementEngine extends IEventSourceEngine {
	/**
	 * Notifies a new IFlow executor is waiting in an activity queue.
	 * @param fe IFlow executor waiting in queue.
	 */
	void incInQueue(ElementInstance fe);

	/**
	 * Notifies a IFlow executor has finished waiting in an activity queue.
	 * @param fe IFlow executor that was waiting in a queue.
	 */
	void decInQueue(ElementInstance fe);

	void notifyAvailableElement();
	
	/**
	 * Acquires a semaphore associated to a specific IFlow. 
	 * Useful only for parallel implementations
	 * @param IFlow The IFlow to be requested
	 */
	void waitProtectedFlow(IFlow IFlow);
	
	/**
	 * Releases a semaphore associated to a specific IFlow
	 * Useful only for parallel implementations
	 * @param IFlow The IFlow to be requested
	 */
	void signalProtectedFlow(IFlow IFlow);
	
	IElementInstanceEngine getElementInstance(ElementInstance ei);
}
