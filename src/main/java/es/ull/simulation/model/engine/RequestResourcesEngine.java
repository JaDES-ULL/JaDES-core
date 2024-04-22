/**
 * 
 */
package es.ull.simulation.model.engine;

import java.util.ArrayDeque;

import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.QueuedObject;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.flow.RequestResourcesFlow.ActivityWorkGroup;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public interface RequestResourcesEngine extends QueuedObject<ElementInstance> {
	/**
	 * Checks whether there is a combination of available resources that satisties the 
	 * requirements of a workgroup
	 * @param solution Tentative solution with booked resources
	 * @param wg 
	 * @param fe
	 * @return
	 */
	boolean checkWorkGroup(ArrayDeque<Resource> solution, ActivityWorkGroup wg, ElementInstance fe);
}
