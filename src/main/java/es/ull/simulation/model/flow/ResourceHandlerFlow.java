/**
 * 
 */
package es.ull.simulation.model.flow;

import es.ull.simulation.model.Resource;

/**
 * A flow step that can interact with {@link Resource resources} 
 * @author Iv�n Castilla
 *
 */
public interface ResourceHandlerFlow extends ActionFlow {
	/**
	 * Returns a unique identifier for the set of resources that handles
	 * @return a unique identifier for the set of resources that handles
	 */
	int getResourcesId();
}
