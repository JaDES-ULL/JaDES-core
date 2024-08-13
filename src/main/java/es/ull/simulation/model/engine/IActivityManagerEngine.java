/**
 * 
 */
package es.ull.simulation.model.engine;

import es.ull.simulation.model.ActivityManager;
import es.ull.simulation.model.ElementInstance;

/**
 * The engine of an {@link ActivityManager activity manager}. Useful for implementing different strategies, such as parallel and sequential.
 * @author Iván Castilla Rodríguez
 *
 */
public interface IActivityManagerEngine {
	/**
     * Informs the activities of new available resources. Reviews the queue of waiting element instances 
     * looking for those which can be executed with the new available resources. The element instances 
     * used are removed from the waiting queue.<p>
     * In order not to traverse the whole list of element instances, this method determines the
     * amount of "useless" ones, that is, the amount of element instances belonging to an activity 
     * which can't be performed with the current resources. If this amount is equal to the size
     * of waiting element instances, this method stops. 
     */
	void processAvailableResources();
	
	/**
	 * Checks if there are new elements available and executes the corresponding actions.
	 * This method centralizes the execution of this code to preserve all the elements and activities priorities.
	 */
	void processAvailableElements();
	
	/**
	 * Notifies the engine that there are new resources available
	 */
	void notifyAvailableResource();
	
	/**
	 * Returns true if there is at least one new resource available the current timestamp
	 * @return true if there is at least one new resource available the current timestamp
	 */
	boolean getAvailableResource();

    /**
     * Adds an element instance to the waiting queue.
     * @param ei Element instance which is added to the waiting queue.
     */
    void queueAdd(ElementInstance ei);
    
    /**
     * Removes an element instance from the waiting queue.
     * @param ei Element instance which is removed from the waiting queue.
     */
    void queueRemove(ElementInstance ei);
    
    /**
     * Notifies the engine that an element is now available to perform activities
     * @param ei Element instance 
     */
    void notifyAvailableElement(ElementInstance ei);

}
