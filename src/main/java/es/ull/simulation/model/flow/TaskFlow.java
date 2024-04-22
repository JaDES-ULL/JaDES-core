/**
 * 
 */
package es.ull.simulation.model.flow;

import es.ull.simulation.model.ElementInstance;

/**
 * A {@link Flow} which executes some kind of work. A task flow is both an {@link InitializerFlow}
 * and a {@link FinalizerFlow}. After being requested, it must perform some kind of work, and when 
 * this work is finished, it must notify its end.<p>
 * @author Iván Castilla Rodríguez
 *
 */
public interface TaskFlow extends InitializerFlow, FinalizerFlow {
	/**
	 * Finishes the associated task.
	 * @param ei The element instance which requested this flow.
	 */
	void finish(final ElementInstance ei);

}
